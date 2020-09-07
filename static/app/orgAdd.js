Vue.component("orgAdd-page", {
	data: function () {
		    return {
		    	orgData : {},
		    	greska : '',
		        nameErr: '',
		        descErr: '',
		        imageErr: ''
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
					<td><input type="text"  v-model="orgData.name" required/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td>Description</td>
					<td><input type="text"  v-model="orgData.description" required /></td>
					<td style="color: red">{{descErr}}</td>
				</tr>
				<tr>
					<td>Logo</td>
					<td><input type="file" @change="onUpload" required/></td>
					<td style="color: red">{{imageErr}}</td>
				</tr>
			</tbody>
			</table>
			
			<a href="#/org" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<a class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="save(orgData)"> Save </a>




		</div>		  
		`
		, 
		methods : {
			
			cancel : function(){
				this.$router.push('/org')
			},
			
			save : function (orgData) {
				this.nameErr = '';
				this.descErr = '';
				this.imageErr = '';
				this.greska = '';
				
				
				if(!this.orgData.name)
					this.nameErr = 'Name cannot be blank.';
				if(!this.orgData.description)
					this.descErr = 'Description cannot be blank.';
				if(this.orgData.imagePath == null)
					this.orgData.imagePath = 'weather_cloud_cloudy_icon_124152.ico';
				if(this.orgData.name && this.orgData.description && this.orgData.imagePath != null){
					axios
					.post('rest/addOrg', {"name": orgData.name, "description": orgData.description, "imagePath": orgData.imagePath})
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/org');
						}

				})
				.catch((res)=>{
					toast("Error");
				})
			}
			},
			onUpload(event) {
				this.orgData.imagePath = (event.target.files)[0].name;
			
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
			.get('rest/testSuperadmin')
			.then((res) => {
				if(res.status == 200){
					
				}				
			})
			.catch((res)=>{
				this.$router.push('/')
			})
		}
	});