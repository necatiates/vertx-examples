

/*
 * 
 * {
 *   oNode
 *   iFinalX
 *   iFinalY
 *   cbEaseX
 *   cbEaseY
 *   iDuration
 *   
 * }
 * 
 */

function CTweenController( oConfig ){
    
    var _iCntTime = 0;
    var _oConfig  = oConfig;
    
    var _iStartX = oConfig.oNode.getX();
    var _iStartY = oConfig.oNode.getY();
    
    var _bStart = false;
    
   this.tweenValue = function( fStart, fEnd, fLerp ){
    return fStart + fLerp *( fEnd-fStart);     
  };
    
    this.start = function(){
        _bStart = true;
    }
    
    this.easeInCubic = function(t, b, c, d) {
	var tc=(t/=d)*t*t;
	return b+c*(tc);
}   


    this.easeBackInQuart =  function(t, b, c, d) {
	var ts=(t/=d)*t;
	var tc=ts*t;
	return b+c*(2*ts*ts + 2*tc + -3*ts);
    };

    
    this.update = function(){
        
        if (_bStart == false){
            return false;
        }
        
        var fLerpX, fLerpY, fOffset;
        
        _iCntTime += s_iTimeElaps;
        
        if ( _iCntTime >= _oConfig.iDuration ){
            _iCntTime = _oConfig.iDuration;
            _bStart = false;
        }
        
        fLerpX = this.easeInCubic( _iCntTime, 0 ,1, _oConfig.iDuration);
        
        if ( _iCntTime <= _oConfig.iDuration/2 ){
            fLerpY = this.easeBackInQuart( _iCntTime, 0 ,1, _oConfig.iDuration);
        }else{
            fLerpY = this.easeBackInQuart( _iCntTime-(_oConfig.iDuration/2), 1 ,0, _oConfig.iDuration/2);
        }

        //trace("fLerpY: " + fLerpY );
 
	oConfig.oNode.setX(this.tweenValue( _iStartX, oConfig.iFinalX, fLerpX));
       // oConfig.oNode.setY(_iStartY + (fLerpY*20));
        //trace("Y: "+(_iStartY + fLerpY*20));
    };
    
}