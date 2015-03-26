var BS_businessman_info={
		/**
		 * 单个对象绑定
		 */
		bindData:function (modelObj){
		   if (modelObj!=null) {			  
		        var viewModel = {};	      
		        for (var item in modelObj) {
		        	if(item=="address")
		        	{
//		        		viewModel["prov"]=  ko.observable(modelObj[item].province);
//		        		viewModel["city"]=ko.observable(modelObj[item].city);
//		        		viewModel["dist"]=ko.observable(modelObj[item].dist);
//		        		viewModel["street"]=ko.observable(modelObj[item].street);
		        		if(modelObj[item].province!=undefined)
		        		{
		        			$("#prov").val(modelObj[item].province);
		        			alert(modelObj[item].province);
		        		}
		        		
		        	}
		        	if(item=="sex"){
		        		viewModel["wantsSpam"]=ko.observable(true);
		        	}
		        	viewModel[item]=  ko.observable(modelObj[item]);
		        }    
		        ko.applyBindings(viewModel);
		   }
		   },
		   /**
		    * 加载个人信息
		    */
		loadInfo: function(){
			var ret=AT_businessman_info.loadInfo({});
			
			BS_businessman_info.bindData(ret.value);
			
		},
		/**
		 *   1:拿到select对象： var  myselect=document.getElementById("test");

  2：拿到选中项的索引：var index=myselect.selectedIndex ;             // selectedIndex代表的是你所选中项的index

  3:拿到选中项options的value：  myselect.options[index].value;

  4:拿到选中项options的text：  myselect.options[index].text

		 */
		   saveInfo:function (){
			   var  selectProv=document.getElementById("prov");
			   var  selectCity=document.getElementById("city0");
			   var  selectDist=document.getElementById("dist");
			   var provIndex=selectProv.selectedIndex;
			   var cityIndex=selectCity.selectedIndex;
			   var distIndex=selectDist.selectedIndex;
			   
			   var prov=selectProv.options[provIndex].text;
			   var city=selectCity.options[cityIndex].text;
			   var dist=selectDist.options[distIndex].text;
			   var street=$("#street")[0].value;
			   
			
			   var imgName=$("#photo")[0].value;
			   var gender=document.getElementsByName("gender");
			   var sex;
			   if(gender.item(0).checked){
					sex="male";
				}else{
					sex="female";
				}
			   alert("shopId"+$("#shopid")[0].value);
			   if(imgName==""||(imgName!=""&&(imgName.indexOf(".png")>-1||imgName.indexOf(".jpg")>-1||imgName.indexOf(".gif")>-1||imgName.indexOf(".jpeg")>-1))){
					var ret=AT_businessman_info.saveInfo("?id="+$("#shopid")[0].value+"&username="+$("#shopname")[0].value+"&email="+$("#email")[0].value+"&phone="+$("#phone")[0].value+"&prov="+prov+"&city="+city
						+"&dist="+dist+"&street="+street+"&addressId="+"0"+"&indroduce="+$("#shopdescription")[0].value+"&sex="+sex+"&power="+$("#power")[0].value);	
					alert(ret.value);
				}else{
						alert("请上传png|jpg|gif|jpeg格式文件");
				}
			  
		   },
		   infoCallBack : function(ret){
				alert(ret.value.address.street);
				location.reload(true);
			},
};