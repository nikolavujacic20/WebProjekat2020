Vue.component("userAdd-page", {
	data: function () {
		    return {
		      user: {organization : null},
		      orgs: null,
		      role: null,
		      organization: {},
		      greska: '',
		      emailErr: '',
		      nameErr: '',
		      lastnameErr: '',
		      passwordErr: '',
		      organizationErr: '',
		     
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
					<td><input type="text"  v-model="user.email"/></td>
					<td style="color: red">{{emailErr}}</td>
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
				<tr v-if="role=='SUPERADMIN'">
					<td>Organization</td>
					<td>
						<select id="selectOrg" v-model="organization.name">
							<option v-for="o in orgs" :value="o.name">{{o.name}}</option>
						</select>
					</td>
				</tr>
				<tr>
					<td>Role</td>
					<td>
						<select id="selectRole" v-model="user.role">
							<option value="ADMIN">ADMIN</option>
							<option value="USER">USER</option>
						</select>
					</td>
				</tr>
				
				
				
			</tbody>
			</table>
			
			<a href="/#/users" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<button class="btn btn-primary btn-lg" tabindex="-1"  v-on:click="save()">Save</button> 

		</div>		  
		`
		, 
		methods : {

			cancel : function(){
				this.$router.push('/users')
			},
			
			save : function(){
				this.user.organization = this.organization;
				
				this.emailErr = '';
				this.nameErr = '';
				this.lastnameErr = '';
				this.passwordErr = '';
				
				if(this.user.email == '')
					this.emailErr = 'Email cannot be blank.';
 				if(this.user.name=='')
					this.nameErr = 'Name cannot be blank.';
				if(this.user.lastName=='')
					this.lastnameErr = 'Last name cannot be blank.';
				if(!this.user.password)
					this.passwordErr = 'Password cannot be blank.';
				
				if(this.user.email && this.user.name && this.user.lastName && this.user.password){
					axios
					.post('rest/addUser', this.user)
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
			.get('rest/testSuperadminAdmin')
			.then((res) => {
				if(res.status == 200){
					
				}				
			})
			.catch((res)=>{
				this.$router.push('/')
			})
	        axios
	          .get('rest/getRole')
	          .then(res => {
	        	  this.role = res.data;
	
	          })
	          
	           axios
	          .get('rest/getAllOrg')
	          .then(res => {
	        	  this.orgs = res.data;
	
	          })
	    },
	});