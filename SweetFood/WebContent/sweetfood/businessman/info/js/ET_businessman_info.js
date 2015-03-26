var ET_businessman_info={
		init : function() {
			
			ET_businessman_info.register();
		},
		register : function() {
			BS_businessman_info.loadInfo();
			$("#saveChange").bind("click",BS_businessman_info.saveInfo);
			} 
};