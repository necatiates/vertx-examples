function CHelp(oSprite){
    var _oGroup;
    var _oText1;
    var _oText1Back;
    var _oText2;
    var _oText2Back;
    
    this._init = function(oSprite){
        var oBg = createBitmap(oSprite);

        var oSprite = s_oSpriteLibrary.getSprite('but_generic');
        _oBuyCardBut = CTextToggle(CANVAS_WIDTH/2,485,oSprite,TEXT_BUYCARD,FONT_GAME,"#ffffff",30,false,s_oStage);
        var oParent = this;
        _oBuyCardBut.addEventListener(ON_MOUSE_UP,oParent._onExit, this);
        var pBetPos = {x:CANVAS_WIDTH/2,y:418};

        _oBetText = new createjs.Text(TEXT_CARD_VALUE,"bold 28px "+FONT_GAME, "#ffffff");
        _oBetText.x = pBetPos.x;
        _oBetText.y = pBetPos.y-140;
        _oBetText.textAlign="center";
        _oBetText.textBaseline = "middle";
        _oBetText.shadow = new createjs.Shadow("#000000", 2, 2, 2);

        var oSprite = s_oSpriteLibrary.getSprite('plus_display');
        _oBetDisplay = new CTextToggle(pBetPos.x,pBetPos.y,oSprite,BET[0].formatDecimal(2, ".", ",")+TEXT_CURRENCY,FONT_GAME,"#ffffff",40,true,s_oStage);
        _oBetDisplay.setScale(0.6);
        _oBetDisplay.block(true);
        _oBetDisplay.setTextPosition(110,38);

        var oSprite = s_oSpriteLibrary.getSprite('but_plus');
        _oButMinus = new CTextToggle(pBetPos.x - 86,pBetPos.y,oSprite,TEXT_MINUS,FONT_GAME,"#ffffff",40,false,s_oStage);
        _oButMinus.addEventListener(ON_MOUSE_UP, this._onButMinRelease, this);
        _oButMinus.setScale(0.6);
        _oButMinus.setScaleX(-1);
        _oButMinus.setTextPosition(-1,-5);

        var oSprite = s_oSpriteLibrary.getSprite('but_plus');
        _oButPlus = new CTextToggle(pBetPos.x + 86,pBetPos.y,oSprite,TEXT_PLUS,FONT_GAME,"#ffffff",40,false,s_oStage);
        _oButPlus.addEventListener(ON_MOUSE_UP, this._onButPlusRelease, this);
        _oButPlus.setScale(0.6);
        _oButPlus.setTextPosition(-1,-5);

        _oText1Back = new createjs.Text(TEXT_COLLECT,"bold 40px "+FONT_GAME, "#000000");
        _oText1Back.x = CANVAS_WIDTH/2 + 2;
        _oText1Back.y = 242;
        _oText1Back.textAlign = "center";
        
        _oText1 = new createjs.Text(TEXT_COLLECT,"bold 40px "+FONT_GAME, "#ffffff");
        _oText1.x = CANVAS_WIDTH/2;
        _oText1.y = 240;
        _oText1.textAlign = "center";
        
        _oText2Back = new createjs.Text(TEXT_AVOID,"bold 40px "+FONT_GAME, "#000000");
        _oText2Back.x = CANVAS_WIDTH/2 + 2;
        _oText2Back.y = 422;
        _oText2Back.textAlign = "center";
        
        _oText2 = new createjs.Text(TEXT_AVOID,"bold 40px "+FONT_GAME, "#ffffff");
        _oText2.x = CANVAS_WIDTH/2;
        _oText2.y = 420;
        _oText2.textAlign = "center";
        
        _oGroup = new createjs.Container();
        _oGroup.addChild(oBg);
        _oGroup.addChild(_oText1Back);
        _oGroup.addChild(_oText2Back);
        _oGroup.addChild(_oText1);
        _oGroup.addChild(_oText2);
        _oGroup.addChild(_oText2);

        //_oGroup.addChild(_oBetText);
        
        var oParent = this;
        _oGroup.on("pressup",function(){oParent._onExit()});
    };
    
    this.unload = function(){
        var oParent = this;
        s_oStage.removeChild(_oGroup);
        _oBuyCardBut.unload();
        _oBetDisplay.unload();
        _oButMinus.unload();
        _oButPlus.unload();
    };
    
    this._onExit = function(){
        this.unload();
        s_oGame.updateNuggets();
        s_oGame._initNuggets();
        s_oGame.onExitHelp();
    };
    this._onButPlusRelease = function(){
        _oBetDisplay.setText( s_oGame.selectBet("add").formatDecimal(2, ".", ",")+TEXT_CURRENCY);
    };

    this._onButMinRelease = function(){
        _oBetDisplay.setText( s_oGame.selectBet("remove").formatDecimal(2, ".", ",")+TEXT_CURRENCY);
    };
    
    this._init(oSprite);
}