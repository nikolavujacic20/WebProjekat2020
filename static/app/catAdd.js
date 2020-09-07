Vue.component("catAdd-page", {
	data: function () {
		    return {
		    	  cat: {},
			      greska: '',
			      nameErr: '',
			      coreNoErr: '',
			      RAMErr: '',
			      GPUErr: ''
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
					<td><input type="text"  v-model="cat.name"/></td>
					<td style="color: red">{{nameErr}}</td>
				</tr>
				<tr>
					<td>Number of cores</td>
					<td><input type="number" v-model = "cat.coreNumber"/></td>
					<td style="color: red">{{coreNoErr}}</td>
				</tr>
				<tr>
					<td>RAM</td>
					<td><input type="number" v-model="cat.RAM"/></td>
					<td style="color: red">{{RAMErr}}</td>
				</tr>
				<tr>
					<td>GPU</td>
					<td><input type="number" v-model="cat.GPUcore"/></td>
					<td style="color: red">{{GPUErr}}</td>
				</tr>
			</tbody>
			</table>
			
			<a href="#/cat" class="btn btn-primary btn-lg" tabindex="-1" role="button" v-on:click="cancel()"> Cancel </a>
			<button class="btn btn-primary btn-lg" tabindex="-1" v-on:click="save(cat)"> Save </button>




		</div>		  
		`
		, 
		methods : {
			
			cancel : function(){
				this.$router.push('/cat')
			},
			
			save : function (cat) {
				this.greska ='';
			    this.nameErr = '';
			    this.coreNoErr = '';
			    this.RAMErr = '';
			    this.GPUErr = '';
				
				if(this.cat.name=='')
					this.nameErr = 'Name cannot be blank.';
				if(!this.cat.coreNumber)
					this.coreNoErr = 'Core number cannot be blank.';
				if(!this.cat.RAM)
					this.RAMErr = 'RAM cannot be blank.';
				if(!this.cat.GPUcore)
					this.GPUErr = 'GPU cannot be blank.';
				if(this.cat.name && this.cat.coreNumber && this.cat.RAM && this.cat.GPUcore){
					axios
					.post('rest/addCat', {"name": cat.name, "coreNumber": cat.coreNumber, "RAM": cat.RAM, "GPUcore": cat.GPUcore})
					.then((res) => {
						if(res.status == 200){
					        this.greska = '';
							this.$router.push('/cat');
						}
					})
					.catch((res)=>{
						toast("Error");
					})
					
					
				}
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