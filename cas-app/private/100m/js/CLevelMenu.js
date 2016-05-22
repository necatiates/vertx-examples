function CLevelMenu(){
    
    
    var _bNumActive;
    
    var _oLevelText;
    var _aLevels = new Array();
    var _oModeNumOff;
    var _oModeNumOn;
    
    var _oBg;
    var _oButExit;
    var _oAudioToggle;
    
    var _pStartPosExit;
    var _pStartPosAudio;
    
    this._init = function(){  
        _oBg = createBitmap(s_oSpriteLibrary.getSprite('bg_select_challenge'));
        s_oStage.addChild(_oBg);
        
        var oFade = new createjs.Shape();
        oFade.graphics.beginFill("rgba(0,0,0,0.6)").drawRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        s_oStage.addChild(oFade);
        
        _bNumActive = false;


	


        var pBetPos = {x:CANVAS_WIDTH/2,y:418};
		

		
        var oSprite = s_oSpriteLibrary.getSprite('select_challenge');
        _oBg = createBitmap(oSprite);
        _oBg.x = CANVAS_WIDTH/2;
        _oBg.y = CANVAS_HEIGHT/2;
        _oBg.regX = oSprite.width/2;
        _oBg.regY = oSprite.height/2;
        s_oStage.addChild(_oBg);
                
        _oLevelText = new createjs.Text("PARA : " + s_oMain.getMoney()," 20px "+FONT, "#ffffff");
        _oLevelText.x = CANVAS_WIDTH/2-180;
        _oLevelText.y = 128;
        _oLevelText.textAlign = "left";
        _oLevelText.textBaseline = "alphabetic";
        _oLevelText.lineWidth = 1000;
        s_oStage.addChild(_oLevelText);
        
        var oModePos = {x: CANVAS_WIDTH/2-240, y: 100};
        
        var offset_x = 0;
        var offset_y = 50;
		
        
        _oBetText = new createjs.Text(TEXT_CARD_VALUE,"bold 28px "+FONT, "#ffffff");
        _oBetText.x = pBetPos.x;
        _oBetText.y = pBetPos.y-170;
        _oBetText.textAlign="center";
        _oBetText.textBaseline = "middle";
        _oBetText.shadow = new createjs.Shadow("#000000", 2, 2, 2);
        s_oStage.addChild(_oBetText);
        
        var oSprite = s_oSpriteLibrary.getSprite('plus_display');
        _oBetDisplay = new CTextToggle(pBetPos.x,pBetPos.y-120,oSprite,BET[0].formatDecimal(2, ".", ",")+TEXT_CURRENCY,FONT,"#ffffff",40,true,s_oStage);        
        _oBetDisplay.setScale(0.6);
        _oBetDisplay.block(true);
        _oBetDisplay.setTextPosition(110,38);        

        var oSprite = s_oSpriteLibrary.getSprite('but_plus');
        _oButMinus = new CTextToggle(pBetPos.x - 86,pBetPos.y-120,oSprite,TEXT_MINUS,FONT,"#ffffff",40,false,s_oStage);
        _oButMinus.addEventListener(ON_MOUSE_UP, this._onButMinRelease, this);
        _oButMinus.setScale(0.6);
        _oButMinus.setScaleX(-1);
        _oButMinus.setTextPosition(-1,-5);
        
        var oSprite = s_oSpriteLibrary.getSprite('but_plus');
        _oButPlus = new CTextToggle(pBetPos.x + 86,pBetPos.y-120,oSprite,TEXT_PLUS,FONT,"#ffffff",40,false,s_oStage);
        _oButPlus.addEventListener(ON_MOUSE_UP, this._onButPlusRelease, this);        
        _oButPlus.setScale(0.6);
        _oButPlus.setTextPosition(-1,-5);
        
		 var oSprite = s_oSpriteLibrary.getSprite('but_continue_small');
        _pStartPosContinue = {x: (CANVAS_WIDTH/2+340), y: CANVAS_HEIGHT-140};
        _oButContinue = new CGfxButton( _pStartPosContinue.x, _pStartPosContinue.y, oSprite, s_oStage );
        _oButContinue.addEventListener(ON_MOUSE_UP, this._onButNextRelease, this);
		
        var oExitX;        
        
        var oSprite = s_oSpriteLibrary.getSprite('but_exit');
        _pStartPosExit = {x: CANVAS_WIDTH - (oSprite.height/2)- 10, y: (oSprite.height/2) + 10};
        _oButExit = new CGfxButton(_pStartPosExit.x, _pStartPosExit.y, oSprite, s_oStage);
        _oButExit.addEventListener(ON_MOUSE_UP, this._onExit, this);
        
        oExitX = CANVAS_WIDTH - (oSprite.width/2)- 90;
        _pStartPosAudio = {x: oExitX, y: (oSprite.height/2) + 10};
        
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            var oSprite = s_oSpriteLibrary.getSprite('audio_icon');
            _oAudioToggle = new CToggle(_pStartPosAudio.x,_pStartPosAudio.y,oSprite,s_bAudioActive);
            _oAudioToggle.addEventListener(ON_MOUSE_UP, this._onAudioToggle, this);          
        }
        
        this.refreshButtonPos(s_iOffsetX,s_iOffsetY);
        
    };  
	
	this._onButNextRelease = function(){
		  s_oLevelMenu.unload();
          s_oMain.gotoGame(0);
	}
    
    this.unload = function(){
      
        s_oLevelMenu = null;
        s_oStage.removeAllChildren();        
    };
    
    this.refreshButtonPos = function(iNewX,iNewY){
        _oButExit.setPosition(_pStartPosExit.x - iNewX,iNewY + _pStartPosExit.y);
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            _oAudioToggle.setPosition(_pStartPosAudio.x - iNewX,iNewY + _pStartPosAudio.y);
        }        
    };
	
	 this._onButPlusRelease = function(){
		 _oBetDisplay.setText(s_oMain.selectBet("add").formatDecimal(2, ".", ",")+TEXT_CURRENCY);
    };
    
    this._onButMinRelease = function(){
		_oBetDisplay.setText(s_oMain.selectBet("remove").formatDecimal(2, ".", ",")+TEXT_CURRENCY);
    };
    
    this._onNumModeToggle = function(iData){
        if(iData === NUM_ACTIVE){
            _bNumActive = true;
            _oModeNumOff.setActive(false);
            _oModeNumOn.setActive(true);
            
        }else {
            _bNumActive = false;
            _oModeNumOff.setActive(true);
            _oModeNumOn.setActive(false);
        }
    };
    
    this._onAudioToggle = function(){
        createjs.Sound.setMute(s_bAudioActive);
        s_bAudioActive = !s_bAudioActive;
    };
    
    this._onMouseDown = function(i){
        _aLevels[i].scaleText();
    };
    
    this._onClick = function(i){
        playSound("press_button",1,0);
        
        var level = i;
        var clickable = _aLevels[i].ifClickable();
        if(clickable){
            s_oLevelMenu.unload();
            s_oMain.gotoGame( level);
        }
    };
     
    this._onExit = function(){
        s_oLevelMenu.unload();
        if(s_iLevelReached === 1){
            s_oMain.gotoMenu();
            $(s_oMain).trigger("end_session");
        }else{
            s_oMain.gotoPlayerProgress();
        }
        
    };   
    
    s_oLevelMenu = this;        
    this._init();
    
};

var s_oLevelMenu = null;