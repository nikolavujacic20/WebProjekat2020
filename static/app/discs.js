Vue.component("disc-page", {
	data: function () {
		    return {
		      discs: null,
		      role: null,
		      searchData: {name: "",
		    	  			from: 0,
		    	  			to: 0}
		    }
	},
	template: ` 
		<div>
		<a href="#/page" class="btn btn-primary btn-lg" tabindex="-1" role="button">
		Home</a>
		<a href="#/addDisc" v-if="role!='USER'"  class="pull-right btn btn-primary btn-lg" tabindex="-1" role="button"> Add New </a>
		<br>
		<br>
			<table class="table table-striped" border="1" frame="BOX" rules="NONE">
			<thead>
				<tr>
					<th>Name</th>
					<th>Capacity</th>
					<th>VM</th>
				</tr>
			</thead>
			<tbody>
					
				<tr v-for="d in discs" v-on:click="selectDisc(d)">
					<td>{{d.name}}</td>
					<td>{{d.capacity}}</td>
					<td>{{d.virtualMachine.name}}</td>
				</tr>
			</tbody>
			</table>
			

			<br>
			<table>
			<tr>
			<td><b>Name Search:</b></td><td class="ml-1"><input class="form-control" type="text" style="background-color:lightgray" size="20%"  id="data" v-model="searchData.name"></td>
			<br><br>
			</tr>
			
			<tr>
			<td class="ml-1"><b>Minimum Capacity:</b></td><td><input input type=”text”  style="background-color:lightgray"  class=”number” id="capacityFrom" v-model="searchData.from"></td>
			<tr>
			<td><b>Maximum Capacity:</b></td><td><input input type=”text” class=”number” style="background-color:lightgray" id="capacityTo" v-model="searchData.to"></td>
	
			<br><br>
			</tr>
			</table>
		<button class="pull-left btn btn-primary btn-lg" tabindex="-1" v-on:click="search(searchData)">Search</button>
		
		</div>		  
		`
		,
		methods : {
			selectDisc : function(d){
				this.$router.push('/disc/edit/'+d.name)
			},
			
			search: function(searchData){
				axios
				.put('rest/searchDiscs', this.searchData)
				.then((res) => {
					if(res.status == 200){
						
						this.discs = res.data;
						if(this.discs.length == 0){
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
	          .get('rest/getAllDiscs')
	          .then(res => (this.discs = res.data))
	          
	        axios
	        	.get('rest/getRole')
	        	.then(res => (this.role = res.data))
	    },
	});