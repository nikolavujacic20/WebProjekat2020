Vue.component("account", {
	data: function () {
		    return {
		      id: null,
		      account: {},
		      role: null,
		      repeat: '',
		      emailErr: '',
		      nameErr:'',
		      lastnameErr:'',
		      passwordErr:'',
		      repeatErr: '',
		      greska:''
		      
		    }
	},
	template: ` 
		<div>
		<a href="#/page" class="btn btn-primary btn-lg pull-left" tabindex="-1" role="button">Home</a>
		<br>
		
		{{greska}}
		<br><br>
			<table class="table table-striped"border="1" frame="BOX" rules="NONE">
			<tbody>
					
				<tr>
					<td><b>Email</b></td>
					<td><input type="text"  v-model="account.email"/></td>
					<td style="color: red">{{emailErr}}</td>
				</tr>
				<tr>
					<td><b>Name</b></td>
					<td><input type="text" v-model = "account.name"/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td><b>Lastname</b></td>
					<td><input type="text" v-model = "account.lastName"/></td>
					<td style="color: red">{{lastnameErr}}</td>
				</tr>
				<tr>
					<td><b>Password</b></td>
					<td><input type="password" v-model = "account.password"/></td>
					<td style="color: red">{{passwordErr}}</td>
				</tr>
				<tr>
					<td><b>Password</b></td>
					<td><input type="password" v-model = "repeat"/></td>
					<td style="color: red">{{repeatErr}}</td>
				</tr>
			</tbody>
			</table>
			
			<a href="#/" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="deleteAcc()"> Delete account </a>
			<a href="#/page" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<button class="btn btn-primary btn-lg" tabindex="-1"  v-on:click="save()">Save</button>
			

		</div>		  
		`
		, 
		methods : {
			deleteAcc : function() {
				axios
					.delete('rest/deleteAcc/' + this.id, {data: this.account})
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/');
						}
					})
			},
	
			cancel : function(){
				this.$router.push('/page')
			},
			
			save : function(){
				if(this.account.email=='')
					this.emailErr = 'Email cannot be blank.';
				if(!this.account.name)
					this.nameErr = 'First name cannot be blank.';
				if(!this.account.lastName)
					this.lastnameErr = 'Lastname cannot be blank.';
				if(!this.account.password)
					this.passwordErr = 'Password cannot be blank.';
				if(this.repeat != this.account.password)
					this.repeatErr = 'Password not matching.'
				if(this.account.email && this.account.name && this.account.lastName && this.account.password && this.repeat 
						&& (this.account.password == this.repeat)){
					axios
					.put('rest/editAcc/'+this.id, this.account)
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/page');
						}
					})
					.catch((res)=>{
						this.greska = 'Error'
					})
					
					
				}
				
				
			}
			
		},
		mounted () {
			axios
				.get('rest/testlogin')
				.then((res) => {
					if(res.status == 200){
						this.id = res.data; // cuvamo stari mejl
					}				
				})
				.catch((res)=>{
					this.$router.push('/')
				})
			
	        axios
	          .get('rest/getAccount')
	          .then(res => {this.account = res.data; this.repeat = res.data.password;})
	          
	        axios
	        	.get('rest/getRole')
	        	.then(res => (this.role = res.data))
	        
	        
	    },
	});