Vue.component("vmAdd-page", {
	data: function () {
		    return {
		    	vmData : {},
		    	discs: null,
		    	categories: null,
		    	organizations: null,
		    	category: {name: ""},
		    	org: {name: ""},
		    	greska : '',
		        nameErr: '',
		        descErr: '',
		        imageErr: '',
		        categoryErr: '',
		        orgErr: '',
		        checkedNames: [],
		        role: null
		        
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
					<td><input type="text"  v-model="vmData.name" required/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td>Category</td>
					<td><select id="selectCategory" v-model="category.name" v-bind:disabled="role=='USER'">
						<option v-for="c in categories" :value="c.name">{{c.name}}</option>
						</select></td>
					<td style="color: red">{{categoryErr}}</td>
				</tr>
				<tr><td>Discs</td><td></td></tr>
				<tr v-for="(d, index) in discs">
					<td><input type="checkbox" id="d.name" value="d.name"  v-on:change="change(d, index)"><label for="d.name">{{d.name}}</label></td>
					<td>
					</td>
				</tr>
				<tr v-if="role=='SUPERADMIN'">
					<td>Organization</td>
					<td><select id="selectOrg" v-model="org.name" v-bind:disabled="role=='USER'">
						<option v-for="o in organizations" :value="o.name">{{o.name}}</option>
						</select></td>
					<td style="color: red">{{orgErr}}</td>
				</tr>
			</tbody>
			</table>
			
			<a href="#/vms" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<a class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="save(vmData)"> Save </a>




		</div>		  
		`
		, 
		methods : {
			
			cancel : function(){
				this.$router.push('/vms')
			},
			
			change : function(d, index){
				for(let n of this.checkedNames){
					if(n == d.name){
						this.checkedNames.splice(index,1);
						return;
					}
				}
				this.checkedNames.push(d.name)
			},
			
			save : function (vmData) {
				this.nameErr = '';
				this.categoryErr = '';
				
				if(!this.vmData.name)
					this.nameErr = 'Name cannot be blank.';
				if(!this.category.name)
					this.categoryErr = 'Category cannot be blank.';
				
				if(this.vmData.name && this.category.name){
				axios
				.post('rest/addVM', {"name": this.vmData.name, "category": this.category.name, "checkedDiscs": this.checkedNames, "org": this.org.name})
				.then((res) => {
					if(res.status == 200){
				        this.greska = '';
						this.$router.push('/vms');
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
						//this.$router.push('/');
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
         		.get('rest/getEmptyDiscs')
         		.then(res => (this.discs = res.data))
         	
         	axios
         		.get('rest/getAllCat')
         		.then(res=> (this.categories = res.data))
         
         	axios
         		.get('rest/getRole')
         		.then(res => (this.role = res.data))
        	
         	axios
         		.get('rest/getAllOrg')
         		.then(res=> (this.organizations = res.data))
        	
			
		}
	});