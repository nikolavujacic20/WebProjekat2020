Vue.component("vmEdit-page", {
	data: function () {
		    return {
		      role: null,
		      vm: {},
		      activities: null,
		      discs: null,
		      category: {},
		      status: null,
		      
		      categories: null,
		      greska: '',
		      nameErr: '',
		      categoryErr: '',		      
		    }
	},
	template: ` 
		<div>
		</br>
			{{greska}}
			<table class="table table-striped" border="1" frame="BOX" rules="NONE">
			<tbody>
					
				<tr>
					<td>Name</td>
					<td><input type="text"  v-model="vm.name" v-bind:disabled="role=='USER'"/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td>Category</td>
					<td><select id="selectCategory" v-model="category.name" v-bind:disabled="role=='USER'">
						<option v-for="c in categories" :value="c.name">{{c.name}}</option>
						</select></td>
					<td style="color: red">{{categoryErr}}</td>
				</tr>
				
				<tr v-for="a in activities">
					<td><input type="date" v-bind:value="a.turnOn | inputDateFilter" v-on:input="a.turnOn = getDate($event.target.value)" v-bind:disabled="role !='SUPERADMIN'"/></td>
					<td><input type="date" v-bind:value="a.turnOff | inputDateFilter" v-on:input="a.turnOff = getDate($event.target.value)" v-bind:disabled="role != 'SUPERADMIN'"/></td>
					<td></td>
				</tr>
				
				<tr v-for="(d, index) in discs">
					<td>{{d.name}}</td>
					<td><button v-on:click="removeDisc(index)">Remove</button></td>
					<td></td>
				</tr>
				
			</tbody>
			</table>
			
			<button class="btn btn-primary btn-lg" tabindex="-1" v-on:click="deleteVM()" v-if="role=='SUPERADMIN'"> Delete </button>
			<a href="#/vms" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<button class="btn btn-primary btn-lg" tabindex="-1"  v-on:click="save()" v-bind:disabled="role=='USER'">Save</button> 
			<button class="btn btn-primary btn-lg" tabindex="-1"  v-on:click="onOff()" v-if="role=='ADMIN' && status==true" >Turn off</button>
			<button class="btn btn-primary btn-lg" tabindex="-1"  v-on:click="onOff()" v-if="role=='ADMIN' && status==false" >Turn on</button>

		</div>		  
		`
		, 
		methods : {
			getDate(value) {
	            if (!value) {
	                return null;
	            }
	            return new Date(value);
	            //return moment.utc(value, "x").format('MM/DD/YYYY');
	        },
			deleteVM : function() {
				axios
					.delete('rest/deleteVM/' + this.$route.params.name, {data: this.vm})
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/vms');
						}
					})
			},
	
			cancel : function(){
				this.$router.push('/vms')
			},
			
			save : function(){
				this.nameErr = '';
				
				if(this.vm.name=='')
					this.nameErr = 'Name cannot be blank.';

				if(this.vm.name){
					for(let a of this.activities){
						a.turnOn = moment(a.turnOn).format('lll');
						a.turnOff = moment(a.turnOff).format('lll');
					}
					
					axios
					.put('rest/editVM/'+this.$route.params.name, {"name": this.vm.name, "category": this.category, "discs": this.discs, "activities": this.activities })
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/vms');
						}
					})
					.catch((res)=>{
						this.greska = 'Error'
					})					
				}
			},
			removeDisc : function(i){
				this.discs.splice(i, 1);
			},
			
			onOff: function(){
				axios
				.put('rest/changeStatus/'+this.$route.params.name)
				.then((res) => {
					if(res.status == 200){
						alert("Successfully changed status");
						this.status = !this.status;
						//this.router.push('/vms/edit/'+this.$route.params.name);
					}
				})
				.catch((res)=>{
					this.greska = 'Error'
				})	
			}
			
		},
		mounted () {
			axios
				.get('rest/testlogin')
				.then((res) => {
					if(res.status == 200){
					}				
				})
				.catch((res)=>{
					this.$router.push('/');
				})
			
	        axios
	          .get('rest/getVM/' + this.$route.params.name)
	          .then(res => {this.vm = res.data;
	          				this.activities = res.data.activities;
	          				this.category = res.data.category;
	          }).catch((res)=>{
					this.$router.push('/');
				})
	          
	         axios
	         	.get('rest/getDiscs/'+this.$route.params.name)
	         	.then(res => (this.discs = res.data))
	         	
	         axios
	         .get('rest/getAllCat')
	         .then(res=> (this.categories = res.data))
	         
	         axios
	        	.get('rest/getRole')
	        	.then(res => (this.role = res.data))
	        	
	        axios
	        	.get('rest/getStatus/'+this.$route.params.name)
	        	.then(res => (this.status = res.data))
	    },
	    filters: {
	    	inputDateFilter: function (date) {
	            if (!date) {
	                return '';
	            }
	            date = new Date(date);
	            return date.getFullYear() + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + ('0' + date.getDate()).slice(-2);
	        }
	   	}
	});