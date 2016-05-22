function CChangeLevel(oSpriteBg){
    
    var _oBg;
    var _oCongratsText;
    var _oMsgText;
    var _oTargetText;
    var _oButStart;
    var _oGroup;
    
    this._init = function(oSpriteBg){
        _oBg = createBitmap(oSpriteBg);
        
        _oCongratsText = new createjs.Text(TEXT_WIN,"bold 50px "+FONT_GAME, "#ffffff");
        _oCongratsText.x = CANVAS_WIDTH/2;
        _oCongratsText.y = (CANVAS_HEIGHT/2)-150;
        _oCongratsText.textAlign = "center";
        
        _oMsgText = new createjs.Text("","bold 40px "+FONT_GAME, "#ffffff");
        _oMsgText.x = CANVAS_WIDTH/2;
        _oMsgText.y = (CANVAS_HEIGHT/2) - 30;
        _oMsgText.textAlign = "center";
        
        _oTargetText = new createjs.Text("","bold 30px "+FONT_GAME, "#ffffff");
        _oTargetText.x = CANVAS_WIDTH/2;
        _oTargetText.y = (CANVAS_HEIGHT/2) + 90;
        _oTargetText.textAlign = "center";
       
        _oButStart = new createjs.Shape();
        _oButStart.graphics.beginFill("yellow").drawRect(0,0,CANVAS_WIDTH,CANVAS_HEIGHT);
        _oButStart.alpha = 0.01;
        
        _oGroup = new createjs.Container();
        _oGroup.alpha = 0;
        _oGroup.visible=false;
        
        _oGroup.addChild(_oBg, _oButStart,_oCongratsText,_oMsgText,_oTargetText);
        
        s_oStage.addChild(_oGroup);
        
        var oParent = this;
        _oButStart.on("pressup",function(){oParent._onChangeLevel();}); 
    };
    
    this.show = function(iLevel,iTarget){
        _oMsgText.text = TEXT_LEVEL + " "+iLevel + " "+TEXT_PASSED;
        _oTargetText.text = TEXT_TARGET + ": "+iTarget+"$";
        _oGroup.visible = true;
        
        createjs.Tween.get(_oGroup).to({alpha:1 }, 500).call(function(){
                                                    if(iLevel>1){
                                                        $(s_oMain).trigger("show_interlevel_ad");
                                                    }
                                                });
    };
    
    this._onChangeLevel = function(){
        var oParent = this;
        _oButStart.off("pressup",function(){oParent._onChangeLevel();}); 
        s_oStage.removeChild(_oGroup);
        
        
        s_oGame.changeLevel();
    };
    
    this._init(oSpriteBg);
}