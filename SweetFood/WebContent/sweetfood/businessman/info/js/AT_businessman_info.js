var AT_businessman_info={
	loadInfo: function(json){

		return jQuery.sendSyn("businessmanController", "loadInfo", json, window);
	},						
	
	saveInfo: function(json){
		return jQuery.sendImgSyn("businessmanController","updateInfo",json,BS_businessman_info.infoCallBack,window,"photo");
	}
};