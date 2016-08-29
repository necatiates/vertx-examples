function CGame(oData){
    var _bUpdate = false;
    var _iState;
    var _oDraggingNugget = null;
    var _iDirHook;
    var _iHookSpeed;
    var _iRopeSpeed;
    var _iSlowDown;
    var _iScore = 0;
    var _iCurLevel = 1;
    var _iCurTargetLevel;
    var _iNuggetToRemove;
    var _iTimeElaps;
    var _aNuggets;
    var _aMalus;
    var _pStartPosExit;
    var _pStartPosAudio;
    
    var _oHook;
    var _oHurt;
    var _oBg;
    var _oHelpPanel;
    var _oButExit;
    var _oButStart;
    var _oEndPanel;
    var _oChangeLevel;
    var _oAudioToggle;
    var _oLevelText;
    var _oTimeText;
    var _oTargetText;
    var _oLevelTextBack;
    var _oTimeTextBack;
    var _oTargetTextBack;
    var _oClockSprite;
    var _oLevelSettings;
    var _iCurBet = BET[0];
    var s_iCurCredit;
    var _iCurRes;
    var _oBalanceBck;
    var _oBalance;
    
    this._init = function(){
        $.ajax({
            url: '/bet/info',
            type: 'get',
            async: false,
            success: function (data) {
                s_iCurCredit = data.cash; CASH_CREDIT = data.cash;
            },
        });

        this.changeState(STATE_HOOK_ROTATE);
        
        _oLevelSettings = new CLevelSettings();
        
        _oBg = createBitmap(s_oSpriteLibrary.getSprite('bg_game'));
         s_oStage.addChild(_oBg);




        _oClockSprite = createBitmap(s_oSpriteLibrary.getSprite('clock'));
        _oClockSprite.x = 298;
        _oClockSprite.y = 10;
        s_oStage.addChild(_oClockSprite);



        _oTimeTextBack = new createjs.Text((LEVEL_TIME/1000),"bold 40px "+FONT_GAME, "#000");
        _oTimeTextBack.x = 380;
        _oTimeTextBack.y = 22;
        _oTimeTextBack.textAlign = "center";
        s_oStage.addChild(_oTimeTextBack);
        
        _oTimeText = new createjs.Text((LEVEL_TIME/1000),"bold 40px "+FONT_GAME, "#fff");
        _oTimeText.x = 378;
        _oTimeText.y = 20;
        _oTimeText.textAlign = "center";
        s_oStage.addChild(_oTimeText);

        _oBalanceBck = new createjs.Text("PARA " + s_iCurCredit.toFixed(2) + "₺","bold 25px "+FONT_GAME, "#000");
        _oBalanceBck.x = 520;
        _oBalanceBck.y = 22;
        _oBalanceBck.textAlign = "center";
        s_oStage.addChild(_oBalanceBck);


        _oBalance = new createjs.Text("PARA " + s_iCurCredit.toFixed(2) + "₺","bold 25px "+FONT_GAME, "#fff");
        _oBalance.x = 520;
        _oBalance.y = 24;
        _oBalance.textAlign = "center";
        s_oStage.addChild(_oBalance);


        _iSlowDown = 0;
        _iRopeSpeed = 0.05;
        _iHookSpeed = HOOK_SPEED;

	    _iTimeElaps = LEVEL_TIME;
        _iCurTargetLevel = _oLevelSettings.getLevelTarget(_iCurLevel);
        _iCurTarget = 0;

        _oTargetTextBack = new createjs.Text(_iScore + "₺","bold 40px "+FONT_GAME, "#000");
        _oTargetTextBack.x = 310;
        _oTargetTextBack.y = 82;
        _oTargetTextBack.textAlign = "left";
        s_oStage.addChild(_oTargetTextBack);

        _oTargetText = new createjs.Text(_iScore+"₺","bold 40px "+FONT_GAME, "#fff");
        _oTargetText.x = 308;
        _oTargetText.y = 80;
        _oTargetText.textAlign = "left";
        s_oStage.addChild(_oTargetText);

        var oSpriteHook = s_oSpriteLibrary.getSprite("hook");
        _oHook = new CHook(oSpriteHook);
       
        this._initNuggets();
        
        _oButStart = new createjs.Shape();
        _oButStart.graphics.beginFill("yellow").drawRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);
        _oButStart.alpha = 0.01;
        s_oStage.addChild(_oButStart);
        
        var oParent = this;
        _oButStart.on("pressup",function(){oParent._onStartDig()}); 
        
        _oHurt = new createjs.Shape();
        _oHurt.graphics.beginFill("red").drawRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);
        _oHurt.alpha = 0.1;
        _oHurt.visible =  false;      
        s_oStage.addChild(_oHurt);
        
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            var oSprite = s_oSpriteLibrary.getSprite('audio_icon')
            _pStartPosAudio = {x: CANVAS_WIDTH - 170, y: 60};
            _oAudioToggle = new CToggle(_pStartPosAudio.x,_pStartPosAudio.y,oSprite);
            _oAudioToggle.addEventListener(ON_MOUSE_UP, this._onAudioToggle, this);
        }
        
         var oSprite = s_oSpriteLibrary.getSprite('but_exit');
        _pStartPosExit = {x: CANVAS_WIDTH - (oSprite.width/2) - 10, y: 10+ (oSprite.height/2)};
        _oButExit = new CGfxButton(_pStartPosExit.x,_pStartPosExit.y,oSprite,true);
        _oButExit.addEventListener(ON_MOUSE_UP, this._onExit, this);
        
        oSprite = s_oSpriteLibrary.getSprite('bg_help');
        _oHelpPanel = new CHelp(oSprite);

        var oSprite = s_oSpriteLibrary.getSprite("msg_box");
        _oChangeLevel = new CChangeLevel(oSprite);
        
        this.refreshButtonPos(s_iOffsetX,s_iOffsetY);
    };
    
    this.refreshButtonPos = function(iNewX,iNewY){
        _oButExit.setPosition(_pStartPosExit.x - iNewX,iNewY + _pStartPosExit.y);
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            _oAudioToggle.setPosition(_pStartPosAudio.x - iNewX,iNewY + _pStartPosAudio.y);
        } 
    };
    
    this.unload = function(){
        s_oStage.removeAllChildren(); 
        
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            _oAudioToggle.unload();
            _oAudioToggle = null;
        }
	
        _oButExit.unload(); 
        _oButExit = null;
        
		
        for(var j=0;j<_aNuggets.length;j++){
            _aNuggets[j].unload();
        }
		
        for(var i=0;i<_aMalus.length;i++){
                _aMalus[i].unload();
        }
        
        s_oGame = null;
    };
    this.updateNuggets = function(){
        if(s_iCurCredit < _iCurBet){
            Lobibox.alert('error', {
                title           : 'Yetersiz Bakiye',
                msg: "Bu bahis için yeterli bakiyeniz bulunmamaktadır"
            });
            this._onExit();
            return;
        }

        var bet = {
            bet : _iCurBet,
            minWin : 0,
            gameName :"GoldMiner"
        };
        $.ajax({
            url: '/miner/bet',
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(bet),
            dataType: 'json',
            async: false,
            success: function (data) {
                _iCurRes = data;
            },
        });

        for(var i=0;i<_aNuggets.length;i++){
            _aNuggets[i].unload();
        }

        for(var t=0;t<_aMalus.length;t++){
            _aMalus[t].unload();
        }
        _oLevelSettings.updateNuggets(_iCurRes,_iCurBet);

        _oBalance.text = "PARA " +(s_iCurCredit - _iCurBet).toFixed(2) + "₺";
        _oBalanceBck.text =  "PARA " +(s_iCurCredit - _iCurBet).toFixed(2) + "₺";
    }
    this._initNuggets = function(){
        _aNuggets = new Array();
        _aMalus = new Array();
        
        var aPos = _oLevelSettings.getNuggetPosInLevel(_iCurLevel);
        var aInfo = _oLevelSettings.getNuggetInfoInLevel(_iCurLevel);
        for(var k=0;k<aPos.length;k++){
            var oSprite = s_oSpriteLibrary.getSprite('nugget_'+aInfo[k].type);       
            var oNugget = new CNugget(aPos[k].x,aPos[k].y,aInfo[k].scale,oSprite);
            oNugget.setIScaleMultiplier(_iCurBet * 1);
            _aNuggets.push(oNugget);
        }

        var oSpriteMalus = s_oSpriteLibrary.getSprite('malus'); 
        var aMalusPos = _oLevelSettings.getMalusPosInLevel(_iCurLevel);
        for(var j=0;j<aMalusPos.length;j++){
            var oMalus = new CMalus(aMalusPos[j].x,aMalusPos[j].y,1,oSpriteMalus);
            _aMalus.push(oMalus);
        }
        
        _iNuggetToRemove = aPos.length;
    };
    
    this.changeLevel = function(){
        for(var i=0;i<_aNuggets.length;i++){
            _aNuggets[i].unload();
        }
        
        for(var t=0;t<_aMalus.length;t++){
            _aMalus[t].unload();
        }
        
        _iSlowDown = 0;
        _iHookSpeed = HOOK_SPEED;
	     _oHook.reset();
        
        _iRopeSpeed+=0.025;
        _iTimeElaps = LEVEL_TIME;
        
        _aNuggets = new Array();
        var aPos = _oLevelSettings.getNuggetPosInLevel(_iCurLevel);
        var aInfo = _oLevelSettings.getNuggetInfoInLevel(_iCurLevel);
        for(var k=0;k<aPos.length;k++){
            var oSprite = s_oSpriteLibrary.getSprite('nugget_'+aInfo[k].type);       
            var oNugget = new CNugget(aPos[k].x,aPos[k].y,aInfo[k].scale,oSprite);  
            
            _aNuggets.push(oNugget);
        }
        
        _aMalus= new Array();
        var oSpriteMalus = s_oSpriteLibrary.getSprite('malus');
        var aMalusPos = _oLevelSettings.getMalusPosInLevel(_iCurLevel);
        for(var j=0;j<aMalusPos.length;j++){
            var oMalus = new CMalus(aMalusPos[j].x,aMalusPos[j].y,1,oSpriteMalus);
            _aMalus.push(oMalus);
        }
        
        _iNuggetToRemove = aPos.length;
        
	this.changeState(STATE_HOOK_ROTATE);
        _bUpdate = true;
        
        $(s_oMain).trigger("start_level",_iCurLevel);
    };
    
    this.changeState = function(iState){
        _iState = iState;
        
        switch(_iState){
            case STATE_HOOK_ROTATE:{
                if(_oDraggingNugget){
                    if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
                        playSound("bonus",1,0);
                    }
                    
                    _iScore += new Number(_oDraggingNugget.getValue());
                    _oTargetTextBack.text = _iScore.toFixed(2) + "₺";
                    _oTargetText.text = _iScore.toFixed(2) + "₺";
                    
                    _oDraggingNugget.hide();
                    _oDraggingNugget = null;
                    
                    _iNuggetToRemove--;

                    if(_iNuggetToRemove === 0){
                        this._endLevel();
                    }
                }else if(_oHook){
                    _oHook.reset();
                }				
                break;
            }
        }
    };
    
    this._checkCollision = function( oHook, oNugget ){
        var vHookPos   = oHook.getPos();
        var vNuggetPos = oNugget.getPos();
        var fNuggetRadius = oNugget.getRadius();
        
        var fDistance =  Math.sqrt( ( (vNuggetPos.x - vHookPos.x)*(vNuggetPos.x - vHookPos.x) ) + 
                                    ( (vNuggetPos.y - vHookPos.y)*(vNuggetPos.y - vHookPos.y) ) );
        
        if ( fDistance < fNuggetRadius ){
            return true;
        }else{
            return false;
        }
    };
    
    this.hookOutOfBounds = function(){
        _iSlowDown = 0;
        this.changeState(STATE_HOOK_MOVE_BACK);
    };
    
    
    this._endLevel = function(){
        _bUpdate = false;
	    _iState = -1;
        $(s_oMain).trigger("end_level",_iCurLevel);

        var bet = {
            totalWin: _iScore.toFixed(2),
            id : _iCurRes.id
        };
        $.ajax({
            url: '/miner/accept',
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(bet),
            dataType: 'json',
            async: false,
            success: function (data) {
                if(data._accepted_) {

                }
            },
        });
        this._gameOver();
    };
    
    this.showHurt = function(){
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            playSound("explosion",1,0);
        }
                    
       _oHurt.visible = true;
        var oParent = this;
        
        createjs.Tween.get(_oHurt).to({alpha:0.6 }, 400).call(function() {oParent._resetHurt();});  
    };

    this._resetHurt = function(){
        _oHurt.visible = false;
        _oHurt.alpha = 0.5;
    };

    this.selectBet = function(szType){

        var iIndex;
        for(var i=0; i<BET.length; i++){
            if(BET[i] === _iCurBet){
                iIndex = i;
            }
        }

        if(szType === "add"){
            if(iIndex !== BET.length-1 && BET[iIndex +1] <= s_iCurCredit){
                iIndex++;
            } else{
                iIndex = 0;
            }
        } else {
            if(iIndex !== 0){
                iIndex--;
            } else if(iIndex === 0){
                for(var i=0; i<BET.length; i++){
                    if(BET[i] <= s_iCurCredit){
                        iIndex = i;
                    }else {
                        break;
                    }
                }
            }
        }

        _iCurBet = BET[iIndex];
        return _iCurBet;
    };
    
    this._gameOver = function(){
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            playSound("gameover",1,0);
        }
        _oEndPanel = new CEndPanel(s_oSpriteLibrary.getSprite('msg_box'));
                                   
        _oEndPanel.show(_iScore.toFixed(2),false);
    };
    
    this._win = function(){
        if(DISABLE_SOUND_MOBILE === false || s_bMobile === false){
            playSound("win",1,0);
        }
        _oEndPanel = new CEndPanel(s_oSpriteLibrary.getSprite('msg_box'));
        _oEndPanel.show(_iScore,true);
    };
    
    this._onStartDig = function(){
        if(_iState === STATE_HOOK_ROTATE){
            _iDirHook = (_oHook.getRotationDeg()+90)*0.0174532925;

            this.changeState(STATE_HOOK_MOVE);
        }
    };
    
    this.onExitHelp = function(){
        _bUpdate = true;
    };
    
    this._onExit = function(){
        $(s_oMain).trigger("end_session");
        $(s_oMain).trigger("share_event",_iScore);
        $(s_oMain).trigger("save_score",_iScore);
        this.unload();
        s_oMain.gotoMenu();
    };
    
    this._onAudioToggle = function(){
        createjs.Sound.setMute(!s_bAudioActive);
    };
    
    this.update = function(){
        if(_bUpdate === false){
            return;
        }
        
        _iTimeElaps -= s_iTimeElaps;
        var iCurSec = Math.floor(_iTimeElaps/1000);
	_oTimeText.text = ""+ iCurSec;
        _oTimeTextBack.text = ""+ iCurSec;
        
        if(_iTimeElaps < 0){
            _oTimeText.text = "0";
            _oTimeTextBack.text = "0";
            this._endLevel();
        }
        
        switch(_iState){
            case STATE_HOOK_ROTATE:{
                _oHook.updateRotation(_iRopeSpeed);    
                break;
            }
            case STATE_HOOK_MOVE:{
                _oHook.updateMove(); 
               
                for(var i=0;i<_aNuggets.length;i++){
                    if(_aNuggets[i].isActive() && this._checkCollision(_oHook,_aNuggets[i])){ 
                        //console.log("COLLIDE WITH "+_aNuggets[i].getX()+","+_aNuggets[i].getY()+" WIDTH: "+_aNuggets[i].getWidth());
                        
                        _iSlowDown = Math.floor(_aNuggets[i].getRadius()) / 8;
                        _oDraggingNugget = _aNuggets[i];
                        
                        this.changeState(STATE_HOOK_MOVE_BACK);
                        break;
                    }
                }
                
                for(var j=0;j<_aMalus.length;j++){
                    if(this._checkCollision(_oHook,_aMalus[j])){
                        _iSlowDown = 0;
                        this.showHurt();
                        
                        this.changeState(STATE_HOOK_MOVE_BACK);
                        
                        _iScore -= MALUS_SCORE;
                        if(_iScore<0){
                            _iScore = 0;
                        }
                        _oTargetText.text = _iScore + "₺";
                        _oTargetTextBack.text = _iScore + "₺";
                        
                        _aMalus[j].unload();
                        _aMalus.splice(j,1);
                        break;
                    }
                }
                break;
            }
            case STATE_HOOK_MOVE_BACK:{
                _oHook.updateMoveBack(_iSlowDown);
                if(_oDraggingNugget){
                    _oDraggingNugget.updateMoveBack(_iHookSpeed - _iSlowDown,_iDirHook);
                }
                break;
            }
        }
    };
    
    s_oGame=this;

    MALUS_SCORE = oData.malus_score;
    HOOK_SPEED = oData.hook_speed;
    LEVEL_TIME = oData.level_time;

    this._init();
}

var s_oGame = null;
