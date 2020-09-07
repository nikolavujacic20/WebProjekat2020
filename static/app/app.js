
const LogIn = {template: '<log-in></log-in>'}
const HomePage = {template: '<home-page></home-page>'}
const Organizations = {template: '<organization-page></organization-page>'}
const OrgEdit = {template: '<orgEdit-page></orgEdit-page>'}
const OrgAdd = {template: '<orgAdd-page></orgAdd-page>'}
const Account = {template: '<account></account>'}
const Categories = {template: '<categories></categories>'}
const CatEdit = {template: '<catEdit-page></catEdit-page>'}
const CatAdd = {template: '<catAdd-page></catAdd-page>'}
const Users = {template: '<users-page></users-page>'}
const UserEdit = {template: '<userEdit-page></userEdit-page>'}
const UserAdd = {template: '<userAdd-page></userAdd-page>'}
const Discs = {template: '<disc-page></disc-page>'}
const DiscEdit = {template: '<discEdit-page></discEdit-page>'}
const DiscAdd = {template: '<discAdd-page></discAdd-page>'}
const VMs = {template: '<vms-page></vms-page>'}
const VMEdit = {template: '<vmEdit-page></vmEdit-page>'}
const VMAdd = {template: '<vmAdd-page></vmAdd-page>'}
const MonthlyCheck = {template: '<monthly-page></monthly-page>'}

const router = new VueRouter({
	  mode: 'hash',
	  routes: [
		  {path: '/', component: LogIn},
		  {path: '/page', component: HomePage},
		  {path: '/org', component: Organizations},
		  {path: '/org/edit/:name', component: OrgEdit},
		  {path: '/addOrg', component: OrgAdd},
		  {path: '/account', component: Account},
		  {path: '/cat', component: Categories},
		  {path: '/cat/edit/:name', component: CatEdit},
		  {path: '/addCategory', component: CatAdd},
		  {path: '/users', component: Users},
		  {path: '/user/edit/:email', component: UserEdit},
		  {path: '/addUser', component: UserAdd},
		  {path: '/discs', component: Discs},
		  {path: '/disc/edit/:name', component: DiscEdit},
		  {path: '/addDisc', component: DiscAdd},
		  {path: '/vms', component: VMs},
		  {path: '/vms/edit/:name', component: VMEdit},
		  {path: '/addVMs', component: VMAdd},
		  {path: '/monthlyCheck', component: MonthlyCheck}
	  ]
});

var app = new Vue({
	router,
	el: '#cloud'
});

