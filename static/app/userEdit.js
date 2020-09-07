Vue.component("userEdit-page", {
	data: function () {
		    return {
		      user: {},
		      organization: {},
		      greska: '',
		      nameErr: '',
		      lastnameErr: '',
		      passwordErr: '',
		     
		    }
	},
	template: ` 
		<div>
		</br>
			{{greska}}
			<table class="table table-striped">
			<tbody>
				<tr>
					<td>Email</td>
					<td>{{user.email}}</td>
				</tr>
				<tr>
					<td>Name</td>
					<td><input type="text"  v-model="user.name"/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td>Last Name</td>
					<td><input type="text"  v-model="user.lastName"/></td>
					<td style="color: red">{{lastnameErr}}</td>
				</tr>
				<tr>
					<td>Password</td>
					<td><input type="password"  v-model="user.password"/></td>
					<td style="color: red">{{passwordErr}}</td>
				</tr>
				<tr>
					<td>Organization</td>
					<td>{{organization.name}}</td>
				</tr>
				<tr>
					<td>Role</td>
					<td>
						<select id="selectRole" v-model="user.role">
							<option v-if="user.role=='SUPERADMIN'" value="SUPERADMIN">SUPERADMIN</option>
							<option v-if="user.role!='USER'" value="ADMIN">ADMIN</option>
							<option value="USER">USER</option>
						</select>
					</td>
				</tr>
				
				
				
			</tbody>
			</table>
			
			<a href="/#/users" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="deleteUser()"> Delete user </a>
			<a href="/#/users" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<button class="btn btn-primary btn-lg" tabindex="-1"  v-on:click="save()">Save</button> 

		</div>		  
		`
		, 
		methods : {
			deleteUser : function() {
				axios
					.delete('rest/deleteUser/' + this.$route.params.email, {data: this.user})
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/users');
						}
					}).catch((res)=>{
						toast('Error');
					})
			},
	
			cancel : function(){
				this.$router.push('/users')
			},
			
			save : function(){
				this.nameErr = '';
				this.lastnameErr = '';
				this.passwordErr = '';
				
				if(this.user.name=='')
					this.nameErr = 'Name cannot be blank.';
				if(this.user.lastName=='')
					this.lastnameErr = 'Last name cannot be blank.';
				if(!this.user.password)
					this.passwordErr = 'Password cannot be blank.';
				
				if(this.user.name && this.user.lastName && this.user.password){
					axios
					.put('rest/editUser/'+this.$route.params.email, this.user)
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/users');
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
	          .get('rest/getUser/' + this.$route.params.email)
	          .then(res => {
	        	  this.user = res.data;
	        	  this.organization = res.data.organization;
	          }).catch((res)=>{
					this.$router.push('/')
				})
	    },
	});