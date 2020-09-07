Vue.component("discEdit-page", {
	data: function () {
		    return {
		      role: null,
		      disc: {},
		      vms: null,
		      vm: {},
		      greska: '',
		      nameErr: '',
		      typeErr: '',
		      capacityErr: '', 
		      
		    }
	},
	template: ` 
		<div>
		</br>
			{{greska}}
			<table class="table table-striped">
			<tbody>
					
				<tr>
					<td>Name</td>
					<td><input type="text"  v-model="disc.name" v-bind:disabled="role=='USER'"/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td>Capacity</td>
					<td><input type="number" v-model = "disc.capacity" v-bind:disabled="role=='USER'"/></td>
					<td style="color: red">{{capacityErr}}</td>
				</tr>
				<tr>
					<td>Type</td>
					<td><select id="selectType" v-model="disc.type" v-bind:disabled="role=='USER'">
						<option value="SSD">SSD</option>
						<option value="HDD">HDD</option>
						</select>
					</td>
					<td></td>
				<tr>
					<td>Virtual machine</td>
					<td><select id="selectVM" v-model="vm.name" v-bind:disabled="role=='USER'">
						<option v-for="v in vms" :value="v.name">{{v.name}}</option>
						</select>
					</td>
					<td></td>
				</tr>
				
			</tbody>
			</table>
			
			<button class="btn btn-primary btn-lg" tabindex="-1" v-on:click="deleteDisc()" v-if="role=='SUPERADMIN'"> Delete </button>
			<a href="#/discs" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<button class="btn btn-primary btn-lg" tabindex="-1"  v-on:click="save()" v-bind:disabled="role=='USER'">Save</button> 

		</div>		  
		`
		, 
		methods : {
			deleteDisc : function() {
				axios
					.delete('rest/deleteDisc/' + this.$route.params.name, {data: this.disc})
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/discs');
						}
					})
			},
	
			cancel : function(){
				this.$router.push('/discs')
			},
			
			save : function(){
				this.greska = '';
				this.nameErr = '';
				this.capacityErr='';
				
				if(this.disc.name=='')
					this.nameErr = 'Name cannot be blank.';
				if(!this.disc.capacity)
					this.capacityErr = 'Capacity cannot be blank';
				if(this.disc.name && this.disc.capacity){
					axios
					.put('rest/editDisc/'+this.$route.params.name, {"name": this.disc.name, "type": this.disc.type, "capacity": this.disc.capacity, "virtualMachine": this.vm })
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/discs');
						}
					})
					.catch((res)=>{
						toast('Error');
					})
					
					
				}
				
				
			},
			
		},
		mounted () {
			axios
				.get('rest/testlogin')
				.then((res) => {
					if(res.status == 200){
					}				
				})
				.catch((res)=>{
					this.$router.push('/')
				})
			
	        axios
	          .get('rest/getDisc/' + this.$route.params.name)
	          .then(res => {this.disc = res.data;
	          				this.vm = res.data.virtualMachine;
	          }).catch((res)=>{
					this.$router.push('/');
				})
			
	         axios
	         	.get('rest/getAllVM')
	         	.then(res => (this.vms = res.data))
	         axios
	        	.get('rest/getRole')
	        	.then(res => (this.role = res.data))
	    },
	});