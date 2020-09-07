Vue.component("discAdd-page", {
	data: function () {
		    return {
		      disc: {virtualMachine: {name:''}, name:'', capacity: '', type:''},
		      vms: null,
		      role: null,
		      vm: {name: ''},
		      greska: '',
		      nameErr: '',
		      capacityErr: '',
		      typeErr: '',
		      vmErr: '',
		     
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
					<td><input type="text"  v-model="disc.name"/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td>Capacity</td>
					<td><input type="number" v-model = "disc.capacity"/></td>
					<td style="color: red">{{capacityErr}}</td>
				</tr>
				<tr>
					<td>Type</td>
					<td><select id="selectType" v-model="disc.type">
						<option value="SSD">SSD</option>
						<option value="HDD">HDD</option>
						</select>
					</td>
					<td style="color: red">{{typeErr}}</td>
				<tr>
					<td>Virtual machine</td>
					<td><select id="selectVM" v-model="vm.name">
						<option v-for="v in vms" :value="v.name">{{v.name}}</option>
						</select>
					</td>
					<td style="color: red">{{vmErr}}</td>
				</tr>
				
			</tbody>
			</table>
			
			<a href="/#/discs" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<button class="btn btn-primary btn-lg" tabindex="-1"  v-on:click="save()">Save</button> 

		</div>		  
		`
		, 
		methods : {

			cancel : function(){
				this.$router.push('/discs')
			},
			
			save : function(){
				this.disc.virtualMachine.name = this.vm.name;
				
				greska = false;
				this.nameErr = '';
				this.capacityErr='';
				this.typeErr='';
				this.vmErr='';
				if(this.disc.name == ''){
					this.nameErr = 'Name cannot be blank.';
					greska = true;
				}
 				if(!this.disc.capacity){
					this.capacityErr = 'Capacity cannot be blank.';
					greska = true;
 				}
				if(!this.disc.type){
					this.typeErr = 'Choose disc type.';
					greska = true;
				}
				if(!this.vm.name){
					this.vmErr = 'Choose virtual machine.';
					greska = true;
				}
				if(greska){
					return;
				}
				axios
				.post('rest/addDisc', this.disc)
				.then((res) => {
					if(res.status == 200){
				        this.greska = '';
						this.$router.push('/discs');
					}
				})
				.catch((res)=>{
					this.greska = 'Error';
					toast('Error');
				})
		
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
			.get('rest/testSuperadminAdmin')
			.then((res) => {
				if(res.status == 200){
					
				}				
			})
			.catch((res)=>{
				this.$router.push('/')
			})
		
         axios
         	.get('rest/getAllVM')
         	.then(res => (this.vms = res.data))
         axios
        	.get('rest/getRole')
        	.then(res => (this.role = res.data))
	    },
	});