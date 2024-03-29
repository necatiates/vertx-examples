function CSlotSettings(){
    
    this._init = function(){
        this._initSymbolSpriteSheets();
        this._initPaylines();
        this._initSymbolWin();
        this._initBets();
        this._initSymbolAnims();
        this._initSymbolsOccurence();
    };
    
    this._initSymbolSpriteSheets = function(){
        s_aSymbolData = new Array();
        for(var i=1;i<NUM_SYMBOLS+1;i++){
            var oData = {   // image to use
                            images: [s_oSpriteLibrary.getSprite('symbol_'+i)], 
                            // width, height & registration point of each sprite
                            frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                            animations: {  static: [0, 1],moving:[1,2] }
            };

            s_aSymbolData[i] = new createjs.SpriteSheet(oData);
        }  
    };

    this._initBets = function(){
      s_aBetCombination = new Array();
      s_aBetCombination[0] = 0.25;
      s_aBetCombination[1] = 0.50;
      s_aBetCombination[2] = 1;
      s_aBetCombination[3] = 2;
      s_aBetCombination[4] = 3;
      s_aBetCombination[5] = 5;
      s_aBetCombination[6] = 10;
    };
    
    this._initPaylines = function(){
        //STORE ALL INFO ABOUT PAYLINE COMBOS
        s_aPaylineCombo = new Array();
        
        s_aPaylineCombo[0] = [{row:1,col:0},{row:1,col:1},{row:1,col:2},{row:1,col:3},{row:1,col:4}];
        s_aPaylineCombo[1] = [{row:0,col:0},{row:0,col:1},{row:0,col:2},{row:0,col:3},{row:0,col:4}];
        s_aPaylineCombo[2] = [{row:2,col:0},{row:2,col:1},{row:2,col:2},{row:2,col:3},{row:2,col:4}];
        s_aPaylineCombo[3] = [{row:0,col:0},{row:1,col:1},{row:2,col:2},{row:1,col:3},{row:0,col:4}];
        s_aPaylineCombo[4] = [{row:2,col:0},{row:1,col:1},{row:0,col:2},{row:1,col:3},{row:2,col:4}];
        s_aPaylineCombo[5] = [{row:1,col:0},{row:0,col:1},{row:0,col:2},{row:0,col:3},{row:1,col:4}];
        s_aPaylineCombo[6] = [{row:1,col:0},{row:2,col:1},{row:2,col:2},{row:2,col:3},{row:1,col:4}];
        s_aPaylineCombo[7] = [{row:0,col:0},{row:0,col:1},{row:1,col:2},{row:2,col:3},{row:2,col:4}];
        s_aPaylineCombo[8] = [{row:2,col:0},{row:2,col:1},{row:1,col:2},{row:0,col:3},{row:0,col:4}];
        s_aPaylineCombo[9] = [{row:1,col:0},{row:2,col:1},{row:1,col:2},{row:0,col:3},{row:1,col:4}];
        s_aPaylineCombo[10] = [{row:2,col:0},{row:0,col:1},{row:1,col:2},{row:2,col:3},{row:1,col:4}];
        s_aPaylineCombo[11] = [{row:0,col:0},{row:1,col:1},{row:1,col:2},{row:1,col:3},{row:0,col:4}];
        s_aPaylineCombo[12] = [{row:2,col:0},{row:1,col:1},{row:1,col:2},{row:1,col:3},{row:2,col:4}];
        s_aPaylineCombo[13] = [{row:0,col:0},{row:1,col:1},{row:0,col:2},{row:1,col:3},{row:0,col:4}];
        s_aPaylineCombo[14] = [{row:2,col:0},{row:1,col:1},{row:2,col:2},{row:1,col:3},{row:2,col:4}];
        s_aPaylineCombo[15] = [{row:1,col:0},{row:1,col:1},{row:0,col:2},{row:1,col:3},{row:1,col:4}];
        s_aPaylineCombo[16] = [{row:1,col:0},{row:1,col:1},{row:2,col:2},{row:1,col:3},{row:1,col:4}];
        s_aPaylineCombo[17] = [{row:0,col:0},{row:0,col:1},{row:2,col:2},{row:0,col:3},{row:0,col:4}];
        s_aPaylineCombo[18] = [{row:2,col:0},{row:2,col:1},{row:0,col:2},{row:2,col:3},{row:2,col:4}];
        s_aPaylineCombo[19] = [{row:0,col:0},{row:2,col:1},{row:2,col:2},{row:2,col:3},{row:0,col:4}];

    };
    
    this._initSymbolWin = function(){
        s_aSymbolWin = new Array();
        
        s_aSymbolWin[0] = [0,0,150,250,500];
        s_aSymbolWin[1] = [0,0,50,100,200];
        s_aSymbolWin[2] = [0,5,10,20,50];
        s_aSymbolWin[3] = [0,1,5,10,15];
        s_aSymbolWin[4] = [0,1,5,10,15];
        s_aSymbolWin[5] = [0,1,5,10,15];
        s_aSymbolWin[6] = [0,1,5,10,15];
    };
    
    this._initSymbolAnims = function(){
        s_aSymbolAnims = new Array();
        
        var oData = {   
                        framerate: 20,
                        images: [s_oSpriteLibrary.getSprite('symbol_1_anim')], 
                        // width, height & registration point of each sprite
                        frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                        animations: {  static: [0, 1],anim:[1,14] }
        };

        s_aSymbolAnims[0] = new createjs.SpriteSheet(oData);
        
        oData = {   
                        framerate: 20,
                        images: [s_oSpriteLibrary.getSprite('symbol_2_anim')], 
                        // width, height & registration point of each sprite
                        frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                        animations: {  static: [0, 1],anim:[1,14] }
        };

        s_aSymbolAnims[1] = new createjs.SpriteSheet(oData);
        
        oData = {   
                        framerate: 20,
                        images: [s_oSpriteLibrary.getSprite('symbol_3_anim')], 
                        // width, height & registration point of each sprite
                        frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                        animations: {  static: [0, 1],anim:[1,14] }
        };

        s_aSymbolAnims[2] = new createjs.SpriteSheet(oData);
        
        oData = {   
                        framerate: 20,
                        images: [s_oSpriteLibrary.getSprite('symbol_4_anim')], 
                        // width, height & registration point of each sprite
                        frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                        animations: {  static: [0, 1],anim:[1,14] }
        };

        s_aSymbolAnims[3] = new createjs.SpriteSheet(oData);
        
        oData = {   
                        framerate: 20,
                        images: [s_oSpriteLibrary.getSprite('symbol_5_anim')], 
                        // width, height & registration point of each sprite
                        frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                        animations: {  static: [0, 1],anim:[1,14] }
        };

        s_aSymbolAnims[4] = new createjs.SpriteSheet(oData);
        
        oData = {   
                        framerate: 20,
                        images: [s_oSpriteLibrary.getSprite('symbol_6_anim')], 
                        // width, height & registration point of each sprite
                        frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                        animations: {  static: [0, 1],anim:[1,14] }
        };

        s_aSymbolAnims[5] = new createjs.SpriteSheet(oData);
        
        oData = {   
                        framerate: 20,
                        images: [s_oSpriteLibrary.getSprite('symbol_7_anim')], 
                        // width, height & registration point of each sprite
                        frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                        animations: {  static: [0, 1],anim:[1,14] }
        };

        s_aSymbolAnims[6] = new createjs.SpriteSheet(oData);
        
        oData = {   
                        framerate: 20,
                        images: [s_oSpriteLibrary.getSprite('symbol_8_anim')], 
                        // width, height & registration point of each sprite
                        frames: {width: SYMBOL_SIZE, height: SYMBOL_SIZE, regX: 0, regY: 0}, 
                        animations: {  static: [0, 1],anim:[1,14] }
        };

        s_aSymbolAnims[7] = new createjs.SpriteSheet(oData);
    };
    
    this._initSymbolsOccurence = function(){
        s_aRandSymbols = new Array();
        
        var i;
        //OCCURENCE FOR SYMBOL 1
        for(i=0;i<1;i++){
            s_aRandSymbols.push(1);
        }
        
        //OCCURENCE FOR SYMBOL 2
        for(i=0;i<2;i++){
            s_aRandSymbols.push(2);
        }
        
        //OCCURENCE FOR SYMBOL 3
        for(i=0;i<3;i++){
            s_aRandSymbols.push(3);
        }
        
        //OCCURENCE FOR SYMBOL 4
        for(i=0;i<4;i++){
            s_aRandSymbols.push(4);
        }
        
        //OCCURENCE FOR SYMBOL 5
        for(i=0;i<4;i++){
            s_aRandSymbols.push(5);
        }
        
        //OCCURENCE FOR SYMBOL 6
        for(i=0;i<6;i++){
            s_aRandSymbols.push(6);
        }
        
        //OCCURENCE FOR SYMBOL 7
        for(i=0;i<6;i++){
            s_aRandSymbols.push(7);
        }
        
        //OCCURENCE FOR SYMBOL WILD
        for(i=0;i<1;i++){
            s_aRandSymbols.push(8);
        }
    };
    
    this._init();
}

var s_aSymbolData;
var s_aPaylineCombo;
var s_aSymbolWin;
var s_aSymbolAnims;
var s_aRandSymbols;
var s_aBetCombination;