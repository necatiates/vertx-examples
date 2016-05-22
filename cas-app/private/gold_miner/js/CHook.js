function CHook(oSpriteHookClosed){
    var _iRotSpeed;
    var _iRotFactor;
    var _iXPos;
    var _iYPos;
    var _iDir;
    var _iMinRopeLen = 50;
    
    var _fRopeSpeed = HOOK_SPEED;
    
    
    var _iRopeLen = 0;
    
    var _vDir = {x:0,y:1};
    var _oLine;
    var _oLineGfx;
    var _oHookSprite;
    
    this._init = function(oSpriteHookClosed){
        _iRotFactor = 0;
        _iRotSpeed = 2;
       
        _oLineGfx = new createjs.Graphics();

        _oLineGfx.setStrokeStyle(2);
        _oLineGfx.beginStroke("#000");
        _oLineGfx.moveTo(HOOK_START_X,HOOK_START_Y);
        _oLineGfx.lineTo(HOOK_START_X,HOOK_START_Y+_iMinRopeLen);

        _oLine = new createjs.Shape(_oLineGfx);
        s_oStage.addChild(_oLine);
        
        _oHookSprite = createBitmap(oSpriteHookClosed);
        _oHookSprite.x = HOOK_START_X;
        _oHookSprite.y = HOOK_START_Y;
        _oHookSprite.regX = oSpriteHookClosed.width/2;
        _oHookSprite.regY = 0;      
        s_oStage.addChild(_oHookSprite);
        
        this._drawLine();
    };
	
	this.reset = function(){
		_iRopeLen = 0;
		_iXPos = HOOK_START_X;
        _iYPos = HOOK_START_Y+_iMinRopeLen;
	};
    
    this.normalize = function(v){
        
        var len = Math.sqrt( v.x*v.x+v.y*v.y );
        if (len > 0 ){
            v.x/= len; v.y/=len; 
        }
    };
    
    this.rotateVector2D = function( iAngle, v ) {		
            var iX = v.x *   Math.cos( iAngle )  + v.y * Math.sin( iAngle );
            var iY = v.x * (-Math.sin( iAngle )) + v.y * Math.cos( iAngle );		
            v.x = iX;
            v.y = iY ;
    };
    
    this.toDegree = function(iAngleRad){
        return iAngleRad * (180/Math.PI);
    };
    
    this._drawLine = function(){
        var fLerp = Math.sin(_iRotFactor);
        _vDir.x = 0; 
        _vDir.y = 1;
        
        var fCurAngle = ((Math.PI/2)*0.8) *fLerp;
        
        this.rotateVector2D( fCurAngle,_vDir);
        this.normalize(_vDir);
        var iNewX = _vDir.x * ( _iMinRopeLen + _iRopeLen);
        var iNewY = _vDir.y * ( _iMinRopeLen + _iRopeLen);
        
        _iXPos = HOOK_START_X+iNewX;
        _iYPos = HOOK_START_Y+iNewY;
        
        _oLineGfx.clear();
        _oLineGfx.setStrokeStyle(1);
        _oLineGfx.beginStroke("#000");
        _oLineGfx.moveTo(HOOK_START_X,HOOK_START_Y);
        _oLineGfx.lineTo(_iXPos,_iYPos);
        
        _oHookSprite.x = _iXPos;
        _oHookSprite.y = _iYPos;

        _oHookSprite.rotation = -(this.toDegree(fCurAngle));
    };

    this.updateRotation = function(iSpeed){
        _iRotFactor += iSpeed;
        
        this._drawLine();
    };
    
    this.updateMove = function(){
        
        _iRopeLen += _fRopeSpeed;
        
        this._drawLine();
        
        if(_oHookSprite.y > CANVAS_HEIGHT || _oHookSprite.x < 0 || _oHookSprite.x>CANVAS_WIDTH){
            s_oGame.hookOutOfBounds();
        }
    };
    
    this.updateMoveBack = function(iSlowDown){
        _iRopeLen -= (_fRopeSpeed-iSlowDown);

        this._drawLine();
        
        if( _iRopeLen < 0 ){
            s_oGame.changeState(STATE_HOOK_ROTATE);
        } 
    };
    
    this.getPos = function(){
        return { x: _iXPos,
                 y: _iYPos};
    };

    
    this.getCurDir = function(){
        return _iDir;
    };
    
    this.getRotationDeg = function(){
        return _oHookSprite.rotation;
    };
    
    this._init(oSpriteHookClosed);
}
