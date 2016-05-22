function CMalus(iXPos,iYPos,iRandScale,oSprite){
    var _iWidth;
    var _iHeight;
    var _oMalusSprite;
    
    this._init = function(iXPos,iYPos,iRandScale,oSprite){
        _oMalusSprite = createBitmap(oSprite);
        _oMalusSprite.x = iXPos;
        _oMalusSprite.y = iYPos;
        _oMalusSprite.scaleX = _oMalusSprite.scaleY = iRandScale;
        _oMalusSprite.regX = oSprite.width/2;
        _oMalusSprite.regY = oSprite.height/2;
        
        s_oStage.addChild(_oMalusSprite);
        
        _iWidth = oSprite.width;
        _iHeight = oSprite.height;
    };
    
    this.changePos = function(iXPos,iYPos,iRandScale){
        _oMalusSprite.x = iXPos;
        _oMalusSprite.y = iYPos;
        _oMalusSprite.scaleX = _oMalusSprite.scaleY = iRandScale;
        _oMalusSprite.visible = true;
    };
    
    this.unload = function(){
        s_oStage.removeChild(_oMalusSprite);
        _oMalusSprite = null;
    };
    
    this.getPos = function(){
        return { x: _oMalusSprite.x, y: _oMalusSprite.y};
    };
    
    this.getRadius = function(){
        return ((_iWidth/2)*iRandScale) * 1;
    };
    
    this._init(iXPos,iYPos,iRandScale,oSprite);
}