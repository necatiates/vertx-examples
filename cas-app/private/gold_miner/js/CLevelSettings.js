function CLevelSettings() {

    var _aNuggetPos;
    var _aNuggetInfo;
    var _aMalusPos;
    var _aLevelTarget;

    this._init = function () {
        _aNuggetPos = new Array();
        _aNuggetInfo = new Array();
        _aMalusPos = new Array();

        //LEVEL 1
        var aNuggets = [{x: 388, y: 470}, {x: 638, y: 250}, {x: 738, y: 350}, {x: 838, y: 550}, {
            x: 1058,
            y: 600
        }, {x: 1138, y: 460}, {x: 1238, y: 680}];
        _aNuggetPos.push(aNuggets);
        var aInfos = [{type: 2, scale: 0.1}, {type: 2, scale: 0.1}, {type: 2, scale: 0.1}, {
            type: 2,
            scale: 0.1
        }, {type: 2, scale: 0.1}, {type: 2, scale: 0.1}, {type: 2, scale: 0.1}];
        _aNuggetInfo.push(aInfos);
        var aMalus = [{x: 938, y: 400}, {x: 938, y: 200}, {x: 338, y: 200}];
        _aMalusPos.push(aMalus);

        //INIT ALL LEVEL TARGET
        _aLevelTarget = new Array();

        //TARGET LEVEL 1
        _aLevelTarget.push(2000);
    };

    this.updateNuggets = function (curRes) {
       this.getRandomNuggets(curRes._win_);
    };

    this.getRandomNuggets = function(win){
        _aNuggetInfo = new Array();
        _aMalusPos = new Array();
        _aNuggetPos = new Array();



        var nuggetsPos = new Array();
        var nuggetsInfo = new Array();
        var malusInfo = new Array();


        if(win){
            for(var i = 0 ; i< 6 ;i ++){
                var nugget = new Object();
                nugget.type = 2;
                nugget.scale = (Math.random() * (0.5 - 0.1) + 0.1).toFixed(1);
                nuggetsInfo.push(nugget);
            }
            for(var i = 0 ; i< 6 ;i ++){
                var nugget = new Object();
                nugget.x = (Math.random() * (900 - 250) + 250).toFixed(0);
                nugget.y = (Math.random() * (700 - 250) + 250).toFixed(0);
                nuggetsPos.push(nugget);
            }
            for(var i = 0 ; i< 3 ;i ++){
                var malus = new Object();
                malus.x = (Math.random() * (900 - 250) + 250).toFixed(0);
                malus.y = (Math.random() * (700 - 250) + 250).toFixed(0);
                malusInfo.push(malus);
            }
        }else{
            for(var i = 0 ; i< 3 ;i ++){
                var nugget = new Object();
                nugget.type = 2;
                nugget.scale = (Math.random() * (0.5 - 0.1) + 0.1).toFixed(1);
                nuggetsInfo.push(nugget);
            }
            for(var i = 0 ; i< 3 ;i ++){
                var nugget = new Object();
                nugget.x = (Math.random() * (900 - 250) + 250).toFixed(0);
                nugget.y = (Math.random() * (700 - 250) + 250).toFixed(0);
                nuggetsPos.push(nugget);
            }
            for(var i = 0 ; i< 6 ;i ++){
                var malus = new Object();
                malus.x = (Math.random() * (900 - 250) + 250).toFixed(0);
                malus.y = (Math.random() * (700 - 250) + 250).toFixed(0);
                malusInfo.push(malus);
            }
        }
        _aNuggetInfo.push(nuggetsInfo);
        _aMalusPos.push(malusInfo);
        _aNuggetPos.push(nuggetsPos);
    };

    this.getNuggetPosInLevel = function (iLevel) {
        return _aNuggetPos[iLevel - 1];
    };

    this.getNuggetInfoInLevel = function (iLevel) {
        return _aNuggetInfo[iLevel - 1];
    };

    this.getMalusPosInLevel = function (iLevel) {
        return _aMalusPos[iLevel - 1];
    };

    this.getLevelTarget = function (iLevel) {
        return _aLevelTarget[iLevel - 1];
    };

    this.getNumLevels = function () {
        return _aLevelTarget.length;
    };

    this._init();
}