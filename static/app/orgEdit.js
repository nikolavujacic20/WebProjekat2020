Vue.component("orgEdit-page", {
	data: function () {
		    return {
		      org: {},
		      greska: '',
		      nameErr: '',
		      descErr: '',
		      imageErr: ''
		    }
	},
	template: ` 
		<div>
		<br><br>
			{{greska}}
			<table class="table table-striped" border="2" frame="BOX" rules="NONE">
			<tbody>
					
				<tr>
					<td><b>Name</b></td>
					<td><input type="text"  v-model="org.name"/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td><b>Description</b></td>
					<td><input type="text" v-model = "org.description"/></td>
					<td style="color: red">{{descErr}}</td>
				</tr>
				<tr>
					<td><b>Logo</b></td>
					<td><input type="file" @change = "onUpload"/></td>
					<td style="color: red">{{imageErr}}</td>
				</tr>
			</tbody>
			</table>
			
			<a href="#/org" class="btn btn-primary btn-lg pull-right" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			
			<button margin-left="15%" class="btn btn-primary btn-lg pull-right" tabindex="-1"  v-on:click="save()">Save</button> 

		</div>		  
		`
		, 
		methods : {
			cancel : function(){
				this.$router.push('/org')
			},
			
			save : function(){
				this.nameErr = '';
				this.descErr = '';
				this.imageErr = '';
				this.greska = '';
				
				if(this.org.name=='')
					this.nameErr = 'Name cannot be blank.';
				if(!this.org.description)
					this.descErr = 'Description cannot be blank.';
				if(this.org.imagePath == null)
					this.imageErr = 'Choose a file for logo.';
				if(this.org.name && this.org.description && this.org.imagePath != null){
					axios
					.put('rest/editOrg/'+this.$route.params.name, this.org)
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/org');
						}
					})
					.catch((res)=>{
						toast('Error');
					})
					
					
				}
				
				
			},
			onUpload(event) {
				this.org.imagePath = (event.target.files)[0].name;
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
					this.$router.push('/')
				})			
			
	        axios
	          .get('rest/getOrg/' + this.$route.params.name)
	          .then(res => (this.org = res.data))
	          .catch((res)=>{
				this.$router.push('/')
			})
	        
	    },
	});