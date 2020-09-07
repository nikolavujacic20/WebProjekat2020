Vue.component("home-page", {
	data: function () {
		    return {
		      vm: null,
		      role: null
		    }
	},
	template: ` 
		<div>
		<div class="header-panel w-100">
			

				<a href="#/account" class="btn btn-primary btn-lg" tabindex="-1" role="button" style="width:20%"> Account</a>
				<a href="#/org" v-if="role!='USER'" class="btn btn-primary btn-lg" tabindex="-1" role="button" style="width:15%"> Organizations</a>
				<a href="#/users"   v-if="role != 'USER'" class="btn btn-primary btn-lg" tabindex="-1" role="button" style="width:15%"> Users</a>
				<a href="#/vms" class="btn btn-primary btn-lg" tabindex="-1" role="button" style="width:15%"> Virtual machines</a>
				<a href="#/discs" class="btn btn-primary btn-lg" tabindex="-1" role="button" style="width:15%" > Discs</a>
				<a href="#/cat" v-if="role=='SUPERADMIN'" class="btn btn-primary btn-lg" tabindex="-1" role="button" style="width:15%"> Categories</a>
				<a href="#/monthlyCheck" v-if="role=='ADMIN'" class="btn btn-primary btn-lg" tabindex="-1" role="button"  style="width:15%"> Monthly check</a>

				
				
			</div>
		<br>
		<br>

			<table class="table table-striped" border="1" frame="BOX" rules="NONE">
			<thead>
				<tr>
					<th>Name</th>
					<th>Core number</th>
					<th>RAM</th>
					<th>GPU</th>
				</tr>
			</thead>
			<tbody>
					
				<tr v-for="m in vm">
					<td>{{m.name }}</td>
					<td>{{m.category.coreNumber}}</td>
					<td>{{m.category.RAM}}</td>
					<td>{{m.category.GPUcore}}</td>

				</tr>
			</tbody>
			</table>
		
		
		<a href="#/" class="btn btn-primary btn-lg pull-right" tabindex="-1" role="button" v-on:click="logout()"> Logout </a>
		</div>		  
		`
		, 
		methods : {
			logout : function () {
				axios
				.get('rest/logout')
				.then((res) => res.data)
			}
			
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
	          .get('rest/getAllVM')
	          .then(res => (this.vm = res.data))
	          
	        axios
	        	.get('rest/getRole')
	        	.then(res => (this.role = res.data))
	        
	        
	    },
	});