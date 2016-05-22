function CNugget(iXPos,iYPos,iScale,oSprite){
    var _bActive;
    var _iValue;
    var _iWidth;
    var _iHeight;
	var _iScale;
    var _oNuggetSprite;
    var _iScaleMultiplier = 10;
    
    this._init = function(iXPos,iYPos,iScale,oSprite){
        _bActive =true;
        var iRandRot = Math.floor(Math.random()*180);

        _oNuggetSprite = createBitmap(oSprite);
        _oNuggetSprite.x = iXPos;
        _oNuggetSprite.y = iYPos;
        _oNuggetSprite.rotation = iRandRot;
        _oNuggetSprite.regX = oSprite.width/2;
        _oNuggetSprite.regY = oSprite.height/2;
        _oNuggetSprite.scaleX = _oNuggetSprite.scaleY = iScale;

        s_oStage.addChild(_oNuggetSprite);
        
        _iValue = Math.floor(_iScaleMultiplier * iScale);
		_iScale = iScale;
        _iWidth = oSprite.width;
        _iHeight = oSprite.height;
    };
    
    this.unload = function(){
        s_oStage.removeChild(_oNuggetSprite);
        _oNuggetSprite = null;
    };
    
    this.changePos = function(iXPos,iYPos,iScale){
        _oNuggetSprite.x = iXPos;
        _oNuggetSprite.y = iYPos;
        _oNuggetSprite.scaleX = _oNuggetSprite.scaleY = _iScale = iScale;
        _oNuggetSprite.visible = true;
        
        _bActive =true;
    };
    
    this.hide = function(){
		if(_oNuggetSprite !== null){
			_oNuggetSprite.visible = false;
			_bActive =false;
		}
    };
    
    this.getImage = function(){
      return _oNuggetSprite;  
    };

    this.setİScaleMultiplier = function(multiplier){
        _iScaleMultiplier = multiplier;
        _iValue = Math.floor(_iScaleMultiplier * iScale);
    }
    
    this.getRadius = function(){
        return ((_iWidth/2)*_iScale) * 1;
    };
    
    this.getPos = function(){
        return { x: _oNuggetSprite.x, y: _oNuggetSprite.y};
    };
    
    this.getX = function(){
        return _oNuggetSprite.x;
    };
    
    this.getY = function(){
        return _oNuggetSprite.y;
    };
    
    this.getWidth = function(){
        return _iWidth;
    };
    
    this.getValue = function(){
        return _iValue;
    };
    
    this.isActive = function(){
        return _bActive;
    };
    
    this.updateMoveBack = function(iHookSpeed,iDir){
        var iNewX = _oNuggetSprite.x - (iHookSpeed) * Math.cos(iDir);
        var iNewY = _oNuggetSprite.y - (iHookSpeed) * Math.sin(iDir);
        
        _oNuggetSprite.x = iNewX;
        _oNuggetSprite.y = iNewY;
    };
    
    this._init(iXPos,iYPos,iScale,oSprite);
}