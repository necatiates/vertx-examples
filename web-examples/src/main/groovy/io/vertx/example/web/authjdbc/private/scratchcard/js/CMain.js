function CMain(oData){
    var _bUpdate;
    var _iCurResource = 0;
    var RESOURCE_TO_LOAD = 0;
    var _iState = STATE_LOADING;
    var _oData;
    
    var _oPreloader;
    var _oMenu;
    var _oHelp;
    var _oGame;

    this.initContainer = function(){
        s_oCanvas = document.getElementById("canvas");
        s_oStage = new createjs.Stage(s_oCanvas);
        createjs.Touch.enable(s_oStage);
	
        s_oCanvasScratch = document.getElementById("clear-image");
        s_oStageScratch = new createjs.Stage(s_oCanvasScratch);
        createjs.Touch.enable(s_oStageScratch);
        
	s_bMobile = jQuery.browser.mobile;
       
        if(s_bMobile === false){
            s_oStageScratch.enableMouseOver(0);  
            $('body').on('contextmenu', '#clear-image', function(e){ return false; });
        }  
        
        s_iPrevTime = new Date().getTime();

	createjs.Ticker.addEventListener("tick", this._update);
        createjs.Ticker.setFPS(20);
        
        //CHECK IF IS PLAYING ON ANDROID DEFAULT BROWSER
	var nua = navigator.userAgent;
        s_bDefaultAndroid = ((nua.indexOf('Mozilla/5.0') > -1 && nua.indexOf('Android ') > -1 && 
                                        nua.indexOf('AppleWebKit') > -1) && !(nua.indexOf('Chrome') > -1)); 
		
        if(navigator.userAgent.match(/Windows Phone/i)){
			DISABLE_SOUND_MOBILE = true;
        }

        s_oSpriteLibrary  = new CSpriteLibrary();
		
        //ADD PRELOADER
        _oPreloader = new CPreloader();
		
	_bUpdate = true;       
    };
    
    this.soundLoaded = function(){
         _iCurResource++;
	     var iPerc = Math.floor(_iCurResource/RESOURCE_TO_LOAD *100);
        _oPreloader.refreshLoader(iPerc);
		
         if(_iCurResource === RESOURCE_TO_LOAD){
            _oPreloader.unload();

            this.gotoMenu();
         }
    };
    
    this._initSounds = function(){
         if (!createjs.Sound.initializeDefaultPlugins()) {
             return;
         }

        if(navigator.userAgent.indexOf("Opera")>0 || navigator.userAgent.indexOf("OPR")>0){
                createjs.Sound.alternateExtensions = ["mp3"];
                createjs.Sound.addEventListener("fileload", createjs.proxy(this.soundLoaded, this));


                createjs.Sound.registerSound("./sounds/sf_loose.ogg", "loose");
                createjs.Sound.registerSound("./sounds/sf_win.ogg", "win");
                createjs.Sound.registerSound("./sounds/sf_press_but.ogg", "press_but");
                createjs.Sound.registerSound("./sounds/sf_scratch.ogg", "scratch");
                
        }else{
                createjs.Sound.alternateExtensions = ["ogg"];
                createjs.Sound.addEventListener("fileload", createjs.proxy(this.soundLoaded, this));


                createjs.Sound.registerSound("./sounds/sf_loose.mp3", "loose");
                createjs.Sound.registerSound("./sounds/sf_win.mp3", "win");
                createjs.Sound.registerSound("./sounds/sf_press_but.mp3", "press_but");
                createjs.Sound.registerSound("./sounds/sf_scratch.mp3", "scratch");
                
        }
        
        RESOURCE_TO_LOAD += 4;
        
    };

    this._loadImages = function(){
        s_oSpriteLibrary.init( this._onImagesLoaded,this._onAllImagesLoaded, this );

        s_oSpriteLibrary.addSprite("but_play","./sprites/but_play.png");
        s_oSpriteLibrary.addSprite("msg_box","./sprites/msg_box.png");
        s_oSpriteLibrary.addSprite("bg_help","./sprites/bg_help.png");
        s_oSpriteLibrary.addSprite("bg_menu","./sprites/bg_menu.jpg"); 
        s_oSpriteLibrary.addSprite("bg_game","./sprites/bg_game.jpg");
        s_oSpriteLibrary.addSprite("but_exit","./sprites/but_exit.png");
        s_oSpriteLibrary.addSprite("audio_icon","./sprites/audio_icon.png");
        s_oSpriteLibrary.addSprite("fruits_icon","./sprites/fruits_icon.png");
        s_oSpriteLibrary.addSprite("fruits","./sprites/fruits.png");
        s_oSpriteLibrary.addSprite("silver","./sprites/silver.png");
        s_oSpriteLibrary.addSprite("plus_display","./sprites/plus_display.png");
        s_oSpriteLibrary.addSprite("but_plus","./sprites/but_plus.png");
        s_oSpriteLibrary.addSprite("but_generic","./sprites/but_generic.png");
        
        
        RESOURCE_TO_LOAD += s_oSpriteLibrary.getNumSprites();
        s_oSpriteLibrary.loadSprites();
    };
    
    this._onImagesLoaded = function(){
        _iCurResource++;
        var iPerc = Math.floor(_iCurResource/RESOURCE_TO_LOAD *100);
        _oPreloader.refreshLoader(iPerc);
        
        if(_iCurResource === RESOURCE_TO_LOAD){
            _oPreloader.unload();
           
            this.gotoMenu();
        }
    };
    
    this._onAllImagesLoaded = function(){
        
    };
    
    this.onAllPreloaderImagesLoaded = function(){
        this._loadImages();
    };
    
    this.preloaderReady = function(){
        this._loadImages();
        
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            this._initSounds();
        }
    };
    
    this.gotoMenu = function(){
        _oMenu = new CMenu();
        _iState = STATE_MENU;
    };

    this.gotoGame = function(){
        _oGame = new CGame(_oData);   
							
        _iState = STATE_GAME;

    };
    
    this.gotoHelp = function(){
        _oHelp = new CHelp();
        _iState = STATE_HELP;
    };
	
    this.stopUpdate = function(){
        _bUpdate = false;
        createjs.Ticker.paused = true;
        $("#block_game").css("display","block");
    };

    this.startUpdate = function(){
        s_iPrevTime = new Date().getTime();
        _bUpdate = true;
        createjs.Ticker.paused = false;
        $("#block_game").css("display","none");
    };

    
    this._update = function(event){
        if(_bUpdate === false){
                return;
        }
        
        var iCurTime = new Date().getTime();
        s_iTimeElaps = iCurTime - s_iPrevTime;
        s_iCntTime += s_iTimeElaps;
        s_iCntFps++;
        s_iPrevTime = iCurTime;
        
        if ( s_iCntTime >= 1000 ){
            s_iCurFps = s_iCntFps;
            s_iCntTime-=1000;
            s_iCntFps = 0;
        }
                
        if(_iState === STATE_GAME){
            _oGame.update();
        }
        
        s_oStage.update(event);        
    };
    
    s_oMain = this;
    
    _oData = oData;
    
    this.initContainer();
}
var s_bMobile;
var s_bAudioActive = true;
var s_bDefaultAndroid = false;
var s_iCntTime = 0;
var s_iTimeElaps = 0;
var s_iPrevTime = 0;
var s_iCntFps = 0;
var s_iCurFps = 0;

var s_oDrawLayer;
var s_oStage;
var s_oMain;
var s_oSpriteLibrary;
var s_oCanvas;
var s_iCurCredit;

var s_oCanvasScratch;
var s_oStageScratch;
