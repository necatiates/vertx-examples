function CEndPanel(oSpriteBg){
    
    var _oBg;
    var _oScoreText;
    var _oScoreTextBack;
    var _oMsgText;
    var _oMsgTextBack;
    var _oGroup;
    
    this._init = function(oSpriteBg){
        
        _oBg = createBitmap(oSpriteBg);

        _oMsgTextBack = new createjs.Text("","bold 50px "+FONT_GAME, "#000");
        _oMsgTextBack.x = CANVAS_WIDTH/2 + 2;
        _oMsgTextBack.y = (CANVAS_HEIGHT/2)-148;
        _oMsgTextBack.textAlign = "center";

        _oMsgText = new createjs.Text("","bold 50px "+FONT_GAME, "#ffffff");
        _oMsgText.x = CANVAS_WIDTH/2;
        _oMsgText.y = (CANVAS_HEIGHT/2)-150;
        _oMsgText.textAlign = "center";
        
        _oScoreTextBack = new createjs.Text("","bold 50px "+FONT_GAME, "#000");
        _oScoreTextBack.x = CANVAS_WIDTH/2 + 2;
        _oScoreTextBack.y = (CANVAS_HEIGHT/2) +42;
        _oScoreTextBack.textAlign = "center";
        
        _oScoreText = new createjs.Text("","bold 50px "+FONT_GAME, "#ffffff");
        _oScoreText.x = CANVAS_WIDTH/2;
        _oScoreText.y = (CANVAS_HEIGHT/2) +40;
        _oScoreText.textAlign = "center";
        
        _oGroup = new createjs.Container();
        _oGroup.alpha = 0;

        _oGroup.visible=false;
        
        _oGroup.addChild(_oBg, _oScoreTextBack,_oScoreText,_oMsgTextBack,_oMsgText);
        
        s_oStage.addChild(_oGroup);
        
        var oParent = this;
        _oGroup.on("pressup",function(){oParent._onExit()});
    };
    
    this.unload = function(){
        var oParent = this;
        _oGroup.off("pressup",function(){oParent._onExit()});
    };
    
    this.show = function(iScore,bWin){
        if(bWin){
            _oMsgTextBack.text = TEXT_WIN;
            _oMsgText.text = TEXT_WIN;
        }else{
            _oMsgTextBack.text = TEXT_GAME_OVER;
            _oMsgText.text = TEXT_GAME_OVER;
        }
        
        _oScoreTextBack.text = TEXT_SCORE+": "+iScore + "₺";
        _oScoreText.text = TEXT_SCORE+": "+iScore+ "₺";
        _oGroup.visible = true;
        
        createjs.Tween.get(_oGroup).to({alpha:1 }, 500).call(function(){$(s_oMain).trigger("show_interlevel_ad");});
		
	$(s_oMain).trigger("save_score",iScore);
    };
    
    this._onExit = function(){
        s_oGame._onExit();
    };
    
    this._init(oSpriteBg);
}