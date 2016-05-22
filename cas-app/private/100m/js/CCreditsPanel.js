function CCreditsPanel(){
    
    var _oBg;
    var _oButExit;
    var _oMsgText;
    
    var _oFade;
    var _oHitArea;
    
    var _oLink;
    var _oContainer;
    
    var _pStartPosExit;
    
    this._init = function(){
        
        _oContainer = new createjs.Container();
        s_oStage.addChild(_oContainer);
        
        var oBgMenu = createBitmap(s_oSpriteLibrary.getSprite('bg_menu'));
        _oContainer.addChild(oBgMenu);
        
        _oFade = new createjs.Shape();
        _oFade.graphics.beginFill("rgba(0,0,0,0.7)").drawRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        _oContainer.addChild(_oFade);
        
        var oSprite = s_oSpriteLibrary.getSprite('bg_credits');
        _oBg = createBitmap(oSprite);
        _oBg.x = CANVAS_WIDTH/2;
        _oBg.y = CANVAS_HEIGHT/2;
        _oBg.regX = oSprite.width/2;
        _oBg.regY = oSprite.height/2;
        _oContainer.addChild(_oBg);
        
        _oHitArea = new createjs.Shape();
        _oHitArea.graphics.beginFill("#0f0f0f").drawRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        _oHitArea.alpha = 0.01;
        _oContainer.addChild(_oHitArea);
                
        var oSprite = s_oSpriteLibrary.getSprite('but_exit');
        _pStartPosExit = {x: CANVAS_WIDTH/2 +100, y: 124};
        _oButExit = new CGfxButton(_pStartPosExit.x, _pStartPosExit.y, oSprite, _oContainer);
        _oButExit.addEventListener(ON_MOUSE_UP, this.unload, this);


    };
    
    this.unload = function(){
        _oHitArea.off("click", this._onLogoButRelease);
        
        _oButExit.unload(); 
        _oButExit = null;
        


        s_oStage.removeChild(_oContainer);
    };
    
    this._onLogoButRelease = function(){
        window.open("http://www.codethislab.com/index.php?&l=en");
    };
    
    this._init();
    
    
};


