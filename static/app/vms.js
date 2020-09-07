Vue.component("vms-page", {
	data: function () {
		    return {
		      vms: null,
		      organization: {},
		      role: null,
		      searchData: {name:'', coreNumberFrom: 0, coreNumberTo:0,
		    	  			RAMFrom: 0, RAMTo: 0,
		    	  			GPUFrom: 0, GPUTo: 0}
		    }
	},
	template: ` 
		<div>
		<a href="#/page" class="btn btn-primary btn-lg pull-left" tabindex="-1" role="button">Home</a>
		<a href="#/addVMs" v-if="role!='USER'" class="btn btn-primary btn-lg pull-right" tabindex="-1" role="button"> Add New </a>
		<br><br><br>
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
					
				<tr v-for="m in vms" v-on:click="selectVM(m)">
					<td>{{m.name }}</td>
					<td>{{m.category.coreNumber}}</td>
					<td>{{m.category.RAM}}</td>
					<td>{{m.category.GPUcore}}</td>

				</tr>
			</tbody>
			</table>
			
			
			
			<table>
			<tr><td><b>Name Search:</b></td><td><input type="text" style="background-color:lightgray"  id="data" v-model="searchData.name"></td><br><br></tr>
			<tr><td><b>Minimum Cores:&nbsp</b></td><td><input type="number" style="background-color:lightgray" id="coreNumberFrom" v-model="searchData.coreNumberFrom"><br></td>
			<td><b>&nbsp&nbsp&nbsp&nbsp&nbspMaximum Cores:</b></td><td><input type="number" id="coreNumberTo" style="background-color:lightgray" v-model="searchData.coreNumberTo"></td><br></tr>
			
			<tr><td><b>Minimum RAM:</b></td><td><input type="number" style="background-color:lightgray" id="RAMFrom" v-model="searchData.RAMFrom"><br></td>
		
			<td><b>&nbsp&nbsp&nbsp&nbsp&nbspMaximum RAM:</b></td><td><input type="number" style="background-color:lightgray" id="RAMTo" v-model="searchData.RAMTo"></td><br><br></tr>
			
			<tr><td><b>Minimum GPU RAM:&nbsp</b></td><td><input type="number" style="background-color:lightgray" id="GPUFrom" v-model="searchData.GPUFrom"></td>
			<td><b>&nbsp&nbsp&nbsp&nbsp&nbspMaximum GPU RAM:&nbsp</b></td><td><input type="number" style="background-color:lightgray" id="GPUTo" v-model="searchData.GPUTo"></td></tr>
			</table>
			<br>
			<button class="btn btn-primary btn-lg" tabindex="-1" v-on:click="search(searchData)">Search</button>
			
		

		</div>		  
		`
		,
		methods : {
			selectVM : function(v){
				this.$router.push('/vms/edit/'+v.name)
			},
			search: function(searchData){
				axios
				.put('rest/searchVMs', this.searchData)
				.then((res) => {
					if(res.status == 200){
						
						this.vms = res.data;
						if(this.vms.length == 0){
							alert("No result")
						}
					}
				})
				.catch((res)=>{
					alert("No results")
				})
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
	          .then(res => (this.vms = res.data))
	          
	        axios
	        	.get('rest/getRole')
	        	.then(res => (this.role = res.data))
	        
	    },
	});