(function(){
var $wnd = window;
var $doc = $wnd.document;
var $moduleName, $moduleBase;
var _, package_com_google_gwt_core_client_ = 'com.google.gwt.core.client.', package_com_google_gwt_lang_ = 'com.google.gwt.lang.', package_com_google_gwt_sample_kitchensink_client_ = 'com.google.gwt.sample.kitchensink.client.', package_com_google_gwt_user_client_ = 'com.google.gwt.user.client.', package_com_google_gwt_user_client_impl_ = 'com.google.gwt.user.client.impl.', package_com_google_gwt_user_client_ui_ = 'com.google.gwt.user.client.ui.', package_com_google_gwt_user_client_ui_impl_ = 'com.google.gwt.user.client.ui.impl.', package_java_lang_ = 'java.lang.', package_java_util_ = 'java.util.';
function nullMethod(){
}

function equals_3(other){
  return this === other;
}

function hashCode_4(){
  return identityHashCode(this);
}

function toString_10(){
  return this.typeName$ + '@' + this.hashCode$();
}

function Object_0(){
}

_ = Object_0.prototype = {};
_.equals$ = equals_3;
_.hashCode$ = hashCode_4;
_.toString$ = toString_10;
_.toString = function(){
  return this.toString$();
}
;
_.typeName$ = package_java_lang_ + 'Object';
_.typeId$ = 1;
function getModuleBaseURL(){
  return getModuleBaseURL_0();
}

function getTypeName(o){
  return o == null?null:o.typeName$;
}

var sUncaughtExceptionHandler = null;
function getHashCode(o){
  return o == null?0:o.$H?o.$H:(o.$H = getNextHashId());
}

function getHashCode_0(o){
  return o == null?0:o.$H?o.$H:(o.$H = getNextHashId());
}

function getModuleBaseURL_0(){
  return $moduleBase;
}

function getNextHashId(){
  return ++sNextHashId;
}

var sNextHashId = 0;
function $equals(this$static, other){
  if (!instanceOf(other, 2)) {
    return false;
  }
  return equalsImpl(this$static, dynamicCast(other, 2));
}

function $hashCode(this$static){
  return getHashCode(this$static);
}

function createArray(){
  return [];
}

function createObject(){
  return {};
}

function equals(other){
  return $equals(this, other);
}

function equalsImpl(o, other){
  return o === other;
}

function hashCode_0(){
  return $hashCode(this);
}

function toString_0(){
  return toStringImpl(this);
}

function toStringImpl(o){
  if (o.toString)
    return o.toString();
  return '[object]';
}

function JavaScriptObject(){
}

_ = JavaScriptObject.prototype = new Object_0();
_.equals$ = equals;
_.hashCode$ = hashCode_0;
_.toString$ = toString_0;
_.typeName$ = package_com_google_gwt_core_client_ + 'JavaScriptObject';
_.typeId$ = 7;
function $Array(this$static, length, typeId, queryId, typeName){
  this$static.length_0 = length;
  this$static.queryId = queryId;
  this$static.typeName$ = typeName;
  this$static.typeId$ = typeId;
  return this$static;
}

function _set(array, index, value){
  return array[index] = value;
}

function clonify_0(a, length){
  return clonify(a, length);
}

function clonify(a, length){
  return $Array(new Array_0(), length, a.typeId$, a.queryId, a.typeName$);
}

function getIntValue(values, index){
  return values[index];
}

function getValue(values, index){
  return values[index];
}

function getValueCount(values){
  return values.length;
}

function initDims_0(typeName, typeIdExprs, queryIdExprs, dimExprs, defaultValue){
  return initDims(typeName, typeIdExprs, queryIdExprs, dimExprs, 0, getValueCount(dimExprs), defaultValue);
}

function initDims(typeName, typeIdExprs, queryIdExprs, dimExprs, index, count, defaultValue){
  var i, length, result;
  if ((length = getIntValue(dimExprs, index)) < 0) {
    throw new NegativeArraySizeException();
  }
  result = $Array(new Array_0(), length, getIntValue(typeIdExprs, index), getIntValue(queryIdExprs, index), typeName);
  ++index;
  if (index < count) {
    typeName = $substring(typeName, 1);
    for (i = 0; i < length; ++i) {
      _set(result, i, initDims(typeName, typeIdExprs, queryIdExprs, dimExprs, index, count, defaultValue));
    }
  }
   else {
    for (i = 0; i < length; ++i) {
      _set(result, i, defaultValue);
    }
  }
  return result;
}

function initValues(typeName, typeId, queryId, values){
  var i, length, result;
  length = getValueCount(values);
  result = $Array(new Array_0(), length, typeId, queryId, typeName);
  for (i = 0; i < length; ++i) {
    _set(result, i, getValue(values, i));
  }
  return result;
}

function setCheck(array, index, value){
  if (value !== null && array.queryId != 0 && !instanceOf(value, array.queryId)) {
    throw new ArrayStoreException();
  }
  return _set(array, index, value);
}

function Array_0(){
}

_ = Array_0.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_lang_ + 'Array';
_.typeId$ = 8;
function canCast(srcId, dstId){
  return !(!(srcId && typeIdArray[srcId][dstId]));
}

function charToString(x){
  return String.fromCharCode(x);
}

function dynamicCast(src, dstId){
  if (src != null)
    canCast(src.typeId$, dstId) || throwClassCastException();
  return src;
}

function instanceOf(src, dstId){
  return src != null && canCast(src.typeId$, dstId);
}

function narrow_char(x){
  return x & 65535;
}

function narrow_int(x){
  return ~(~x);
}

function round_int(x){
  if (x > ($clinit_233() , MAX_VALUE))
    return $clinit_233() , MAX_VALUE;
  if (x < ($clinit_233() , MIN_VALUE))
    return $clinit_233() , MIN_VALUE;
  return x >= 0?Math.floor(x):Math.ceil(x);
}

function throwClassCastException(){
  throw new ClassCastException();
}

function throwClassCastExceptionUnlessNull(o){
  if (o !== null) {
    throw new ClassCastException();
  }
  return o;
}

function wrapJSO(jso, seed){
  _ = seed.prototype;
  if (jso && !(jso.typeId$ >= _.typeId$)) {
    var oldToString = jso.toString;
    for (var i in _) {
      jso[i] = _[i];
    }
    jso.toString = oldToString;
  }
  return jso;
}

var typeIdArray;
function $addStyleDependentName(this$static, styleSuffix){
  $addStyleName(this$static, $getStylePrimaryName(this$static) + charToString(45) + styleSuffix);
}

function $addStyleName(this$static, style){
  setStyleName_0(this$static.getStyleElement(), style, true);
}

function $getAbsoluteLeft_0(this$static){
  return getAbsoluteLeft(this$static.getElement());
}

function $getAbsoluteTop_0(this$static){
  return getAbsoluteTop(this$static.getElement());
}

function $getOffsetHeight_0(this$static){
  return getElementPropertyInt(this$static.element, 'offsetHeight');
}

function $getOffsetWidth_0(this$static){
  return getElementPropertyInt(this$static.element, 'offsetWidth');
}

function $getStylePrimaryName(this$static){
  return getStylePrimaryName(this$static.getStyleElement());
}

function $removeStyleDependentName(this$static, styleSuffix){
  $removeStyleName(this$static, $getStylePrimaryName(this$static) + charToString(45) + styleSuffix);
}

function $removeStyleName(this$static, style){
  setStyleName_0(this$static.getStyleElement(), style, false);
}

function $replaceNode(this$static, node, newNode){
  var p = node.parentNode;
  if (!p) {
    return;
  }
  p.insertBefore(newNode, node);
  p.removeChild(node);
}

function $setElement_0(this$static, elem){
  if (this$static.element !== null) {
    $replaceNode(this$static, this$static.element, elem);
  }
  this$static.element = elem;
}

function $setSize(this$static, width, height){
  this$static.setWidth(width);
  this$static.setHeight(height);
}

function $setStyleName(this$static, style){
  setStyleName(this$static.getStyleElement(), style);
}

function $sinkEvents_0(this$static, eventBitsToAdd){
  sinkEvents(this$static.getElement(), eventBitsToAdd | getEventsSunk(this$static.getElement()));
}

function getElement_0(){
  return this.element;
}

function getOffsetHeight_0(){
  return $getOffsetHeight_0(this);
}

function getOffsetWidth_1(){
  return $getOffsetWidth_0(this);
}

function getStyleElement_0(){
  return this.element;
}

function getStyleName(elem){
  return getElementProperty(elem, 'className');
}

function getStylePrimaryName(elem){
  var fullClassName, spaceIdx;
  fullClassName = getStyleName(elem);
  spaceIdx = $indexOf_0(fullClassName, 32);
  if (spaceIdx >= 0) {
    return $substring_0(fullClassName, 0, spaceIdx);
  }
  return fullClassName;
}

function setElement_0(elem){
  $setElement_0(this, elem);
}

function setHeight_1(height){
  setStyleAttribute(this.element, 'height', height);
}

function setStyleName(elem, styleName){
  setElementProperty(elem, 'className', styleName);
}

function setStyleName_0(elem, style, add){
  var begin, end, idx, last, lastPos, newClassName, oldStyle;
  if (elem === null) {
    throw $RuntimeException(new RuntimeException(), 'Null widget handle. If you are creating a composite, ensure that initWidget() has been called.');
  }
  style = $trim(style);
  if ($length(style) == 0) {
    throw $IllegalArgumentException(new IllegalArgumentException(), 'Style names cannot be empty');
  }
  oldStyle = getStyleName(elem);
  idx = $indexOf_1(oldStyle, style);
  while (idx != (-1)) {
    if (idx == 0 || $charAt(oldStyle, idx - 1) == 32) {
      last = idx + $length(style);
      lastPos = $length(oldStyle);
      if (last == lastPos || last < lastPos && $charAt(oldStyle, last) == 32) {
        break;
      }
    }
    idx = $indexOf_2(oldStyle, style, idx + 1);
  }
  if (add) {
    if (idx == (-1)) {
      if ($length(oldStyle) > 0) {
        oldStyle += ' ';
      }
      setElementProperty(elem, 'className', oldStyle + style);
    }
  }
   else {
    if (idx != (-1)) {
      begin = $trim($substring_0(oldStyle, 0, idx));
      end = $trim($substring(oldStyle, idx + $length(style)));
      if ($length(begin) == 0) {
        newClassName = end;
      }
       else if ($length(end) == 0) {
        newClassName = begin;
      }
       else {
        newClassName = begin + ' ' + end;
      }
      setElementProperty(elem, 'className', newClassName);
    }
  }
}

function setTitle_0(title){
  if (title === null || $length(title) == 0) {
    removeElementAttribute(this.element, 'title');
  }
   else {
    setElementAttribute(this.element, 'title', title);
  }
}

function setVisible_0(elem, visible){
  elem.style.display = visible?'':'none';
}

function setVisible_1(visible){
  setVisible_0(this.element, visible);
}

function setWidth_2(width){
  setStyleAttribute(this.element, 'width', width);
}

function toString_7(){
  if (this.element === null) {
    return '(null handle)';
  }
  return toString_1(this.element);
}

function UIObject(){
}

_ = UIObject.prototype = new Object_0();
_.getElement = getElement_0;
_.getOffsetHeight = getOffsetHeight_0;
_.getOffsetWidth = getOffsetWidth_1;
_.getStyleElement = getStyleElement_0;
_.setElement = setElement_0;
_.setHeight = setHeight_1;
_.setTitle = setTitle_0;
_.setVisible = setVisible_1;
_.setWidth = setWidth_2;
_.toString$ = toString_7;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'UIObject';
_.typeId$ = 11;
_.element = null;
function $onAttach(this$static){
  if (this$static.isAttached()) {
    throw $IllegalStateException(new IllegalStateException(), "Should only call onAttach when the widget is detached from the browser's document");
  }
  this$static.attached = true;
  setEventListener(this$static.getElement(), this$static);
  this$static.doAttachChildren();
  this$static.onLoad();
}

function $onDetach(this$static){
  if (!this$static.isAttached()) {
    throw $IllegalStateException(new IllegalStateException(), "Should only call onDetach when the widget is attached to the browser's document");
  }
  try {
    this$static.onUnload();
  }
   finally {
    this$static.doDetachChildren();
    setEventListener(this$static.getElement(), null);
    this$static.attached = false;
  }
}

function $removeFromParent(this$static){
  if (instanceOf(this$static.parent, 35)) {
    dynamicCast(this$static.parent, 35).remove_0(this$static);
  }
   else if (this$static.parent !== null) {
    throw $IllegalStateException(new IllegalStateException(), "This widget's parent does not implement HasWidgets");
  }
}

function $setElement_1(this$static, elem){
  if (this$static.isAttached()) {
    setEventListener(this$static.getElement(), null);
  }
  $setElement_0(this$static, elem);
  if (this$static.isAttached()) {
    setEventListener(elem, this$static);
  }
}

function $setLayoutData(this$static, layoutData){
  this$static.layoutData = layoutData;
}

function $setParent(this$static, parent){
  var oldParent;
  oldParent = this$static.parent;
  if (parent === null) {
    if (oldParent !== null && oldParent.isAttached()) {
      this$static.onDetach();
    }
    this$static.parent = null;
  }
   else {
    if (oldParent !== null) {
      throw $IllegalStateException(new IllegalStateException(), 'Cannot set a new parent without first clearing the old parent');
    }
    this$static.parent = parent;
    if (parent.isAttached()) {
      this$static.onAttach();
    }
  }
}

function doAttachChildren_1(){
}

function doDetachChildren_1(){
}

function isAttached_0(){
  return this.attached;
}

function onAttach_2(){
  $onAttach(this);
}

function onBrowserEvent_14(event_0){
}

function onDetach_4(){
  $onDetach(this);
}

function onLoad_3(){
}

function onUnload_2(){
}

function setElement_1(elem){
  $setElement_1(this, elem);
}

function Widget(){
}

_ = Widget.prototype = new UIObject();
_.doAttachChildren = doAttachChildren_1;
_.doDetachChildren = doDetachChildren_1;
_.isAttached = isAttached_0;
_.onAttach = onAttach_2;
_.onBrowserEvent = onBrowserEvent_14;
_.onDetach = onDetach_4;
_.onLoad = onLoad_3;
_.onUnload = onUnload_2;
_.setElement = setElement_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Widget';
_.typeId$ = 12;
_.attached = false;
_.layoutData = null;
_.parent = null;
function $initWidget(this$static, widget){
  if (this$static.widget !== null) {
    throw $IllegalStateException(new IllegalStateException(), 'Composite.initWidget() may only be called once.');
  }
  $removeFromParent(widget);
  this$static.setElement(widget.getElement());
  this$static.widget = widget;
  $setParent(widget, this$static);
}

function getElement(){
  if (this.widget === null) {
    throw $IllegalStateException(new IllegalStateException(), 'initWidget() was never called in ' + getTypeName(this));
  }
  return this.element;
}

function isAttached(){
  if (this.widget !== null) {
    return this.widget.isAttached();
  }
  return false;
}

function onAttach(){
  this.widget.onAttach();
  this.onLoad();
}

function onDetach(){
  try {
    this.onUnload();
  }
   finally {
    this.widget.onDetach();
  }
}

function Composite(){
}

_ = Composite.prototype = new Widget();
_.getElement = getElement;
_.isAttached = isAttached;
_.onAttach = onAttach;
_.onDetach = onDetach;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Composite';
_.typeId$ = 13;
_.widget = null;
function onShow_3(){
}

function Sink(){
}

_ = Sink.prototype = new Composite();
_.onShow = onShow_3;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Sink';
_.typeId$ = 14;
function $Info(this$static){
  $initWidget(this$static, $Label(new Label()));
  return this$static;
}

function init(){
  return $Info$1(new Info$1(), 'Intro', "<h2>Introduction to the Kitchen Sink<\/h2><p>This is the Kitchen Sink sample.  It demonstrates many of the widgets in the Google Web Toolkit.<p>This sample also demonstrates something else really useful in GWT: history support.  When you click on a tab, the location bar will be updated with the current <i>history token<\/i>, which keeps the app in a bookmarkable state.  The back and forward buttons work properly as well.  Finally, notice that you can right-click a tab and 'open in new window' (or middle-click for a new tab in Firefox).<\/p><\/p>");
}

function onShow(){
}

function Info(){
}

_ = Info.prototype = new Sink();
_.onShow = onShow;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Info';
_.typeId$ = 15;
function $Sink$SinkInfo(this$static, name, desc){
  this$static.name_0 = name;
  this$static.description = desc;
  return this$static;
}

function $getInstance(this$static){
  if (this$static.instance !== null) {
    return this$static.instance;
  }
  return this$static.instance = this$static.createInstance();
}

function getColor_1(){
  return '#2a8ebf';
}

function Sink$SinkInfo(){
}

_ = Sink$SinkInfo.prototype = new Object_0();
_.getColor = getColor_1;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Sink$SinkInfo';
_.typeId$ = 16;
_.description = null;
_.instance = null;
_.name_0 = null;
function $Info$1(this$static, $anonymous0, $anonymous1){
  $Sink$SinkInfo(this$static, $anonymous0, $anonymous1);
  return this$static;
}

function createInstance(){
  return $Info(new Info());
}

function Info$1(){
}

_ = Info$1.prototype = new Sink$SinkInfo();
_.createInstance = createInstance;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Info$1';
_.typeId$ = 17;
function $clinit_8(){
  $clinit_8 = nullMethod;
  images_0 = $Sink_Images_generatedBundle(new Sink_Images_generatedBundle());
}

function $$init(this$static){
  this$static.list = $SinkList(new SinkList(), images_0);
  this$static.description = $HTML(new HTML());
  this$static.panel = $VerticalPanel(new VerticalPanel());
}

function $KitchenSink(this$static){
  $clinit_8();
  $$init(this$static);
  return this$static;
}

function $loadSinks(this$static){
  $addSink(this$static.list, init());
  $addSink(this$static.list, init_4(images_0));
  $addSink(this$static.list, init_1(images_0));
  $addSink(this$static.list, init_0(images_0));
  $addSink(this$static.list, init_3());
  $addSink(this$static.list, init_2());
}

function $onHistoryChanged(this$static, token){
  var info;
  info = $find(this$static.list, token);
  if (info === null) {
    $showInfo(this$static);
    return;
  }
  $show(this$static, info, false);
}

function $onModuleLoad(this$static){
  var initToken;
  $loadSinks(this$static);
  $add_7(this$static.panel, this$static.list);
  $add_7(this$static.panel, this$static.description);
  this$static.panel.setWidth('100%');
  $setStyleName(this$static.description, 'ks-Info');
  addHistoryListener(this$static);
  $add(get(), this$static.panel);
  initToken = getToken();
  if ($length(initToken) > 0) {
    $onHistoryChanged(this$static, initToken);
  }
   else {
    $showInfo(this$static);
  }
}

function $show(this$static, info, affectHistory){
  if (info === this$static.curInfo) {
    return;
  }
  this$static.curInfo = info;
  if (this$static.curSink !== null) {
    $remove_7(this$static.panel, this$static.curSink);
  }
  this$static.curSink = $getInstance(info);
  $setSinkSelection(this$static.list, info.name_0);
  $setHTML_0(this$static.description, info.description);
  if (affectHistory) {
    newItem(info.name_0);
  }
  setStyleAttribute(this$static.description.getElement(), 'backgroundColor', info.getColor());
  this$static.curSink.setVisible(false);
  $add_7(this$static.panel, this$static.curSink);
  this$static.panel.setCellHorizontalAlignment(this$static.curSink, ($clinit_108() , ALIGN_CENTER));
  this$static.curSink.setVisible(true);
  this$static.curSink.onShow();
}

function $showInfo(this$static){
  $show(this$static, $find(this$static.list, 'Intro'), false);
}

function onHistoryChanged(token){
  $onHistoryChanged(this, token);
}

function KitchenSink(){
}

_ = KitchenSink.prototype = new Object_0();
_.onHistoryChanged = onHistoryChanged;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'KitchenSink';
_.typeId$ = 18;
_.curInfo = null;
_.curSink = null;
var images_0;
function $clinit_12(){
  $clinit_12 = nullMethod;
  stringLists = initValues('[[Ljava.lang.String;', 207, 24, [initValues('[Ljava.lang.String;', 205, 1, ['foo0', 'bar0', 'baz0', 'toto0', 'tintin0']), initValues('[Ljava.lang.String;', 205, 1, ['foo1', 'bar1', 'baz1', 'toto1', 'tintin1']), initValues('[Ljava.lang.String;', 205, 1, ['foo2', 'bar2', 'baz2', 'toto2', 'tintin2']), initValues('[Ljava.lang.String;', 205, 1, ['foo3', 'bar3', 'baz3', 'toto3', 'tintin3']), initValues('[Ljava.lang.String;', 205, 1, ['foo4', 'bar4', 'baz4', 'toto4', 'tintin4'])]);
  words_0 = initValues('[Ljava.lang.String;', 205, 1, ['1337', 'apple', 'about', 'ant', 'bruce', 'banana', 'bobv', 'canada', 'coconut', 'compiler', 'donut', 'deferred binding', 'dessert topping', 'eclair', 'ecc', 'frog attack', 'floor wax', 'fitz', 'google', 'gosh', 'gwt', 'hollis', 'haskell', 'hammer', 'in the flinks', 'internets', 'ipso facto', 'jat', 'jgw', 'java', 'jens', 'knorton', 'kaitlyn', 'kangaroo', 'la grange', 'lars', 'love', 'morrildl', 'max', 'maddie', 'mloofle', 'mmendez', 'nail', 'narnia', 'null', 'optimizations', 'obfuscation', 'original', 'ping pong', 'polymorphic', 'pleather', 'quotidian', 'quality', "qu'est-ce que c'est", 'ready state', 'ruby', 'rdayal', 'subversion', 'superclass', 'scottb', 'tobyr', 'the dans', '~ tilde', 'undefined', 'unit tests', 'under 100ms', 'vtbl', 'vidalia', 'vector graphics', 'w3c', 'web experience', 'work around', 'w00t!', 'xml', 'xargs', 'xeno', 'yacc', 'yank (the vi command)', 'zealot', 'zoe', 'zebra']);
  fProto = initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto_0(new Lists$Proto(), 'Beethoven', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto_0(new Lists$Proto(), 'Concertos', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'No. 1 - C'), $Lists$Proto(new Lists$Proto(), 'No. 2 - B-Flat Major'), $Lists$Proto(new Lists$Proto(), 'No. 3 - C Minor'), $Lists$Proto(new Lists$Proto(), 'No. 4 - G Major'), $Lists$Proto(new Lists$Proto(), 'No. 5 - E-Flat Major')])), $Lists$Proto_0(new Lists$Proto(), 'Quartets', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'Six String Quartets'), $Lists$Proto(new Lists$Proto(), 'Three String Quartets'), $Lists$Proto(new Lists$Proto(), 'Grosse Fugue for String Quartets')])), $Lists$Proto_0(new Lists$Proto(), 'Sonatas', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'Sonata in A Minor'), $Lists$Proto(new Lists$Proto(), 'Sonata in F Major')])), $Lists$Proto_0(new Lists$Proto(), 'Symphonies', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'No. 1 - C Major'), $Lists$Proto(new Lists$Proto(), 'No. 2 - D Major'), $Lists$Proto(new Lists$Proto(), 'No. 3 - E-Flat Major'), $Lists$Proto(new Lists$Proto(), 'No. 4 - B-Flat Major'), $Lists$Proto(new Lists$Proto(), 'No. 5 - C Minor'), $Lists$Proto(new Lists$Proto(), 'No. 6 - F Major'), $Lists$Proto(new Lists$Proto(), 'No. 7 - A Major'), $Lists$Proto(new Lists$Proto(), 'No. 8 - F Major'), $Lists$Proto(new Lists$Proto(), 'No. 9 - D Minor')]))])), $Lists$Proto_0(new Lists$Proto(), 'Brahms', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto_0(new Lists$Proto(), 'Concertos', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'Violin Concerto'), $Lists$Proto(new Lists$Proto(), 'Double Concerto - A Minor'), $Lists$Proto(new Lists$Proto(), 'Piano Concerto No. 1 - D Minor'), $Lists$Proto(new Lists$Proto(), 'Piano Concerto No. 2 - B-Flat Major')])), $Lists$Proto_0(new Lists$Proto(), 'Quartets', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'Piano Quartet No. 1 - G Minor'), $Lists$Proto(new Lists$Proto(), 'Piano Quartet No. 2 - A Major'), $Lists$Proto(new Lists$Proto(), 'Piano Quartet No. 3 - C Minor'), $Lists$Proto(new Lists$Proto(), 'String Quartet No. 3 - B-Flat Minor')])), $Lists$Proto_0(new Lists$Proto(), 'Sonatas', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'Two Sonatas for Clarinet - F Minor'), $Lists$Proto(new Lists$Proto(), 'Two Sonatas for Clarinet - E-Flat Major')])), $Lists$Proto_0(new Lists$Proto(), 'Symphonies', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'No. 1 - C Minor'), $Lists$Proto(new Lists$Proto(), 'No. 2 - D Minor'), $Lists$Proto(new Lists$Proto(), 'No. 3 - F Major'), $Lists$Proto(new Lists$Proto(), 'No. 4 - E Minor')]))])), $Lists$Proto_0(new Lists$Proto(), 'Mozart', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto_0(new Lists$Proto(), 'Concertos', initValues('[Lcom.google.gwt.sample.kitchensink.client.Lists$Proto;', 206, 38, [$Lists$Proto(new Lists$Proto(), 'Piano Concerto No. 12'), $Lists$Proto(new Lists$Proto(), 'Piano Concerto No. 17'), $Lists$Proto(new Lists$Proto(), 'Clarinet Concerto'), $Lists$Proto(new Lists$Proto(), 'Violin Concerto No. 5'), $Lists$Proto(new Lists$Proto(), 'Violin Concerto No. 4')]))]))]);
}

function $$init_0(this$static){
  this$static.combo = $ListBox(new ListBox());
  this$static.list = $ListBox(new ListBox());
  this$static.oracle = $MultiWordSuggestOracle(new MultiWordSuggestOracle());
  this$static.suggestBox = $SuggestBox(new SuggestBox(), this$static.oracle);
}

function $Lists(this$static, images){
  var horz, i, panel, suggestPanel;
  $clinit_12();
  $$init_0(this$static);
  $setVisibleItemCount(this$static.combo, 1);
  $addChangeListener(this$static.combo, this$static);
  $setVisibleItemCount(this$static.list, 10);
  $setMultipleSelect(this$static.list, true);
  for (i = 0; i < stringLists.length_0; ++i) {
    $addItem(this$static.combo, 'List ' + i);
  }
  $setSelectedIndex(this$static.combo, 0);
  $fillList(this$static, 0);
  $addChangeListener(this$static.list, this$static);
  for (i = 0; i < words_0.length_0; ++i) {
    $add_4(this$static.oracle, words_0[i]);
  }
  suggestPanel = $VerticalPanel(new VerticalPanel());
  $add_7(suggestPanel, $Label_0(new Label(), 'Suggest box:'));
  $add_7(suggestPanel, this$static.suggestBox);
  horz = $HorizontalPanel(new HorizontalPanel());
  $setVerticalAlignment_0(horz, ($clinit_112() , ALIGN_TOP));
  $setSpacing(horz, 8);
  $add_3(horz, this$static.combo);
  $add_3(horz, this$static.list);
  $add_3(horz, suggestPanel);
  panel = $VerticalPanel(new VerticalPanel());
  $setHorizontalAlignment_1(panel, ($clinit_108() , ALIGN_LEFT));
  $add_7(panel, horz);
  $initWidget(this$static, panel);
  this$static.tree = $Tree(new Tree(), images);
  for (i = 0; i < fProto.length_0; ++i) {
    $createItem(this$static, fProto[i]);
    $addItem_6(this$static.tree, fProto[i].item);
  }
  $addTreeListener(this$static.tree, this$static);
  this$static.tree.setWidth('20em');
  $add_3(horz, this$static.tree);
  return this$static;
}

function $createItem(this$static, proto){
  proto.item = $TreeItem_0(new TreeItem(), proto.text);
  $setUserObject(proto.item, proto);
  if (proto.children_0 !== null) {
    proto.item.addItem($Lists$PendingItem(new Lists$PendingItem()));
  }
}

function $fillList(this$static, idx){
  var i, strings;
  $clear_0(this$static.list);
  strings = stringLists[idx];
  for (i = 0; i < strings.length_0; ++i) {
    $addItem(this$static.list, strings[i]);
  }
}

function init_0(images){
  $clinit_12();
  return $Lists$1(new Lists$1(), 'Lists', "<h2>Lists and Trees<\/h2><p>GWT provides a number of ways to display lists and trees. This includes the browser's built-in list and drop-down boxes, as well as the more advanced suggestion combo-box and trees.<\/p><p>Try typing some text in the SuggestBox below to see what happens!<\/p>", images);
}

function onChange(sender){
  if (sender === this.combo) {
    $fillList(this, $getSelectedIndex(this.combo));
  }
   else {
  }
}

function onShow_0(){
}

function onTreeItemSelected(item){
}

function onTreeItemStateChanged(item){
  var child, i, proto;
  child = $getChild_0(item, 0);
  if (instanceOf(child, 4)) {
    item.removeItem(child);
    proto = item.userObject;
    for (i = 0; i < proto.children_0.length_0; ++i) {
      $createItem(this, proto.children_0[i]);
      item.addItem(proto.children_0[i].item);
    }
  }
}

function Lists(){
}

_ = Lists.prototype = new Sink();
_.onChange = onChange;
_.onShow = onShow_0;
_.onTreeItemSelected = onTreeItemSelected;
_.onTreeItemStateChanged = onTreeItemStateChanged;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Lists';
_.typeId$ = 19;
_.tree = null;
var fProto, stringLists, words_0;
function $Lists$1(this$static, $anonymous0, $anonymous1, val$images){
  this$static.val$images = val$images;
  $Sink$SinkInfo(this$static, $anonymous0, $anonymous1);
  return this$static;
}

function createInstance_0(){
  return $Lists(new Lists(), this.val$images);
}

function Lists$1(){
}

_ = Lists$1.prototype = new Sink$SinkInfo();
_.createInstance = createInstance_0;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Lists$1';
_.typeId$ = 20;
function $$init_27(this$static){
  this$static.children_0 = $ArrayList(new ArrayList());
  this$static.statusImage = $Image(new Image_0());
}

function $TreeItem(this$static){
  var tbody, tdContent, tdImg, tr;
  $$init_27(this$static);
  this$static.setElement(createDiv());
  this$static.itemTable = createTable();
  this$static.contentElem = createSpan();
  this$static.childSpanElem = createSpan();
  tbody = createTBody();
  tr = createTR();
  tdImg = createTD();
  tdContent = createTD();
  appendChild(this$static.itemTable, tbody);
  appendChild(tbody, tr);
  appendChild(tr, tdImg);
  appendChild(tr, tdContent);
  setStyleAttribute(tdImg, 'verticalAlign', 'middle');
  setStyleAttribute(tdContent, 'verticalAlign', 'middle');
  appendChild(this$static.getElement(), this$static.itemTable);
  appendChild(this$static.getElement(), this$static.childSpanElem);
  appendChild(tdImg, this$static.statusImage.getElement());
  appendChild(tdContent, this$static.contentElem);
  setStyleAttribute(this$static.contentElem, 'display', 'inline');
  setStyleAttribute(this$static.getElement(), 'whiteSpace', 'nowrap');
  setStyleAttribute(this$static.childSpanElem, 'whiteSpace', 'nowrap');
  setStyleName_0(this$static.contentElem, 'gwt-TreeItem', true);
  return this$static;
}

function $TreeItem_0(this$static, html){
  $TreeItem(this$static);
  $setHTML_2(this$static, html);
  return this$static;
}

function $getChild_0(this$static, index){
  if (index < 0 || index >= this$static.children_0.size) {
    return null;
  }
  return dynamicCast($get_0(this$static.children_0, index), 33);
}

function $getChildIndex(this$static, child){
  return $indexOf_3(this$static.children_0, child);
}

function $getFocusableWidget(this$static){
  var w;
  w = this$static.widget;
  {
    return null;
  }
}

function $getImageElement(this$static){
  return this$static.statusImage.getElement();
}

function $remove_6(this$static){
  if (this$static.parent !== null) {
    this$static.parent.removeItem(this$static);
  }
   else if (this$static.tree !== null) {
    $removeItem_0(this$static.tree, this$static);
  }
}

function $setHTML_2(this$static, html){
  $setWidget_4(this$static, null);
  setInnerHTML(this$static.contentElem, html);
}

function $setParentItem(this$static, parent){
  this$static.parent = parent;
}

function $setSelected(this$static, selected){
  if (this$static.selected == selected) {
    return;
  }
  this$static.selected = selected;
  setStyleName_0(this$static.contentElem, 'gwt-TreeItem-selected', selected);
}

function $setState(this$static, open){
  $setState_0(this$static, open, true);
}

function $setState_0(this$static, open, fireEvents){
  if (open && this$static.children_0.size == 0) {
    return;
  }
  this$static.open = open;
  $updateState(this$static);
  if (fireEvents && this$static.tree !== null) {
    $fireStateChanged(this$static.tree, this$static);
  }
}

function $setTree(this$static, newTree){
  var i, n;
  if (this$static.tree === newTree) {
    return;
  }
  if (this$static.tree !== null) {
    if (this$static.tree.curSelection === this$static) {
      $setSelectedItem(this$static.tree, null);
    }
  }
  this$static.tree = newTree;
  for (i = 0 , n = this$static.children_0.size; i < n; ++i) {
    $setTree(dynamicCast($get_0(this$static.children_0, i), 33), newTree);
  }
  $updateState(this$static);
}

function $setUserObject(this$static, userObj){
  this$static.userObject = userObj;
}

function $setWidget_4(this$static, newWidget){
  setInnerHTML(this$static.contentElem, '');
  this$static.widget = newWidget;
}

function $updateState(this$static){
  var images;
  if (this$static.tree === null) {
    return;
  }
  images = this$static.tree.images;
  if (this$static.children_0.size == 0) {
    setVisible_0(this$static.childSpanElem, false);
    $applyTo(($clinit_28() , treeLeaf_SINGLETON), this$static.statusImage);
    return;
  }
  if (this$static.open) {
    setVisible_0(this$static.childSpanElem, true);
    $applyTo(($clinit_28() , treeOpen_SINGLETON), this$static.statusImage);
  }
   else {
    setVisible_0(this$static.childSpanElem, false);
    $applyTo(($clinit_28() , treeClosed_SINGLETON), this$static.statusImage);
  }
}

function $updateStateRecursive(this$static){
  var i, n;
  $updateState(this$static);
  for (i = 0 , n = this$static.children_0.size; i < n; ++i) {
    $updateStateRecursive(dynamicCast($get_0(this$static.children_0, i), 33));
  }
}

function addItem_0(item){
  if (item.parent !== null || item.tree !== null) {
    $remove_6(item);
  }
  $setParentItem(item, this);
  $add_9(this.children_0, item);
  setStyleAttribute(item.getElement(), 'marginLeft', '16px');
  appendChild(this.childSpanElem, item.getElement());
  $setTree(item, this.tree);
  if (this.children_0.size == 1) {
    $updateState(this);
  }
}

function removeItem_0(item){
  if (!$contains_0(this.children_0, item)) {
    return;
  }
  $setTree(item, null);
  removeChild(this.childSpanElem, item.getElement());
  $setParentItem(item, null);
  $remove_13(this.children_0, item);
  if (this.children_0.size == 0) {
    $updateState(this);
  }
}

function TreeItem(){
}

_ = TreeItem.prototype = new UIObject();
_.addItem = addItem_0;
_.removeItem = removeItem_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TreeItem';
_.typeId$ = 21;
_.childSpanElem = null;
_.contentElem = null;
_.itemTable = null;
_.open = false;
_.parent = null;
_.selected = false;
_.tree = null;
_.userObject = null;
_.widget = null;
function $Lists$PendingItem(this$static){
  $TreeItem_0(this$static, 'Please wait...');
  return this$static;
}

function Lists$PendingItem(){
}

_ = Lists$PendingItem.prototype = new TreeItem();
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Lists$PendingItem';
_.typeId$ = 22;
function $Lists$Proto(this$static, text){
  this$static.text = text;
  return this$static;
}

function $Lists$Proto_0(this$static, text, children){
  $Lists$Proto(this$static, text);
  this$static.children_0 = children;
  return this$static;
}

function Lists$Proto(){
}

_ = Lists$Proto.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Lists$Proto';
_.typeId$ = 23;
_.children_0 = null;
_.item = null;
_.text = null;
function $Panels(this$static, images){
  var c, contents, disc, dock, east, flow, grid, hSplit, horz, i, north0, north1, r, scroller, south, tabs, vert, vp, west;
  contents = $HTML_0(new HTML(), "This is a <code>ScrollPanel<\/code> contained at the center of a <code>DockPanel<\/code>.  By putting some fairly large contents in the middle and setting its size explicitly, it becomes a scrollable area within the page, but without requiring the use of an IFRAME.Here's quite a bit more meaningless text that will serve primarily to make this thing scroll off the bottom of its visible area.  Otherwise, you might have to make it really, really small in order to see the nifty scroll bars!");
  scroller = $ScrollPanel_0(new ScrollPanel(), contents);
  $setStyleName(scroller, 'ks-layouts-Scroller');
  dock = $DockPanel(new DockPanel());
  $setHorizontalAlignment(dock, ($clinit_108() , ALIGN_CENTER));
  north0 = $HTML_1(new HTML(), 'This is the <i>first<\/i> north component', true);
  east = $HTML_1(new HTML(), '<center>This<br>is<br>the<br>east<br>component<\/center>', true);
  south = $HTML_0(new HTML(), 'This is the south component');
  west = $HTML_1(new HTML(), '<center>This<br>is<br>the<br>west<br>component<\/center>', true);
  north1 = $HTML_1(new HTML(), 'This is the <b>second<\/b> north component', true);
  $add_1(dock, north0, ($clinit_87() , NORTH));
  $add_1(dock, east, ($clinit_87() , EAST));
  $add_1(dock, south, ($clinit_87() , SOUTH));
  $add_1(dock, west, ($clinit_87() , WEST));
  $add_1(dock, north1, ($clinit_87() , NORTH));
  $add_1(dock, scroller, ($clinit_87() , CENTER));
  disc = $DisclosurePanel_0(new DisclosurePanel(), 'Click to disclose something:');
  $setContent(disc, $HTML_0(new HTML(), 'This widget is is shown and hidden<br>by the disclosure panel that wraps it.'));
  flow = $FlowPanel(new FlowPanel());
  for (i = 0; i < 8; ++i) {
    $add_2(flow, $CheckBox_1(new CheckBox(), 'Flow ' + i));
  }
  horz = $HorizontalPanel(new HorizontalPanel());
  $setVerticalAlignment_0(horz, ($clinit_112() , ALIGN_MIDDLE));
  $add_3(horz, $Button_0(new Button(), 'Button'));
  $add_3(horz, $HTML_1(new HTML(), '<center>This is a<br>very<br>tall thing<\/center>', true));
  $add_3(horz, $Button_0(new Button(), 'Button'));
  vert = $VerticalPanel(new VerticalPanel());
  $setHorizontalAlignment_1(vert, ($clinit_108() , ALIGN_CENTER));
  $add_7(vert, $Button_0(new Button(), 'Small'));
  $add_7(vert, $Button_0(new Button(), '--- BigBigBigBig ---'));
  $add_7(vert, $Button_0(new Button(), 'tiny'));
  vp = $VerticalPanel(new VerticalPanel());
  $setHorizontalAlignment_1(vp, ($clinit_108() , ALIGN_CENTER));
  $setSpacing(vp, 8);
  $add_7(vp, $makeLabel(this$static, 'Disclosure Panel'));
  $add_7(vp, disc);
  $add_7(vp, $makeLabel(this$static, 'Flow Panel'));
  $add_7(vp, flow);
  $add_7(vp, $makeLabel(this$static, 'Horizontal Panel'));
  $add_7(vp, horz);
  $add_7(vp, $makeLabel(this$static, 'Vertical Panel'));
  $add_7(vp, vert);
  grid = $Grid_0(new Grid(), 4, 4);
  for (r = 0; r < 4; ++r) {
    for (c = 0; c < 4; ++c) {
      $setWidget_0(grid, r, c, $createImage(($clinit_28() , gwtLogo_SINGLETON)));
    }
  }
  tabs = $TabPanel(new TabPanel());
  $add_6(tabs, vp, 'Basic Panels');
  $add_6(tabs, dock, 'Dock Panel');
  $add_6(tabs, grid, 'Tables');
  tabs.setWidth('100%');
  $selectTab_0(tabs, 0);
  hSplit = $HorizontalSplitPanel(new HorizontalSplitPanel());
  $setLeftWidget(hSplit, tabs);
  $setRightWidget(hSplit, $HTML_0(new HTML(), 'This is some text to make the right side of this splitter look a bit more interesting... This is some text to make the right side of this splitter look a bit more interesting... This is some text to make the right side of this splitter look a bit more interesting... This is some text to make the right side of this splitter look a bit more interesting... '));
  $initWidget(this$static, hSplit);
  $setSize(hSplit, '100%', '450px');
  return this$static;
}

function $makeLabel(this$static, caption){
  var html;
  html = $HTML_0(new HTML(), caption);
  $setStyleName(html, 'ks-layouts-Label');
  return html;
}

function init_1(images){
  return $Panels$1(new Panels$1(), 'Panels', "<h2>Panels<\/h2><p>This page demonstrates some of the basic GWT panels, each of which arranges its contained widgets differently.  These panels are designed to take advantage of the browser's built-in layout mechanics, which keeps the user interface snappy and helps your AJAX code play nicely with existing HTML.  On the other hand, if you need pixel-perfect control, you can tweak things at a low level using the <code>DOM<\/code> class.<\/p>", images);
}

function onShow_1(){
}

function Panels(){
}

_ = Panels.prototype = new Sink();
_.onShow = onShow_1;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Panels';
_.typeId$ = 24;
function $Panels$1(this$static, $anonymous0, $anonymous1, val$images){
  this$static.val$images = val$images;
  $Sink$SinkInfo(this$static, $anonymous0, $anonymous1);
  return this$static;
}

function createInstance_1(){
  return $Panels(new Panels(), this.val$images);
}

function getColor(){
  return '#fe9915';
}

function Panels$1(){
}

_ = Panels$1.prototype = new Sink$SinkInfo();
_.createInstance = createInstance_1;
_.getColor = getColor;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Panels$1';
_.typeId$ = 25;
function $$init_1(this$static){
  this$static.dialogButton = $Button_1(new Button(), 'Show Dialog', this$static);
  this$static.popupButton = $Button_1(new Button(), 'Show Popup', this$static);
}

function $Popups(this$static){
  var i, list, panel;
  $$init_1(this$static);
  panel = $VerticalPanel(new VerticalPanel());
  $add_7(panel, this$static.popupButton);
  $add_7(panel, this$static.dialogButton);
  list = $ListBox(new ListBox());
  $setVisibleItemCount(list, 1);
  for (i = 0; i < 10; ++i) {
    $addItem(list, 'list item ' + i);
  }
  $add_7(panel, list);
  $setSpacing(panel, 8);
  $initWidget(this$static, panel);
  return this$static;
}

function init_2(){
  return $Popups$1(new Popups$1(), 'Popups', "<h2>Popups and Dialog Boxes<\/h2><p>This page demonstrates GWT's built-in support for in-page popups.  The first is a very simple informational popup that closes itself automatically when you click off of it.  The second is a more complex draggable dialog box. If you're wondering why there's a list box at the bottom, it's to demonstrate that you can drag the dialog box over it (this obscure corner case often renders incorrectly on some browsers).<\/p>");
}

function onClick_0(sender){
  var dlg, left, p, top;
  if (sender === this.popupButton) {
    p = $Popups$MyPopup(new Popups$MyPopup());
    left = $getAbsoluteLeft_0(sender) + 10;
    top = $getAbsoluteTop_0(sender) + 10;
    $setPopupPosition(p, left, top);
    $show_0(p);
  }
   else if (sender === this.dialogButton) {
    dlg = $Popups$MyDialog(new Popups$MyDialog());
    $center(dlg);
  }
}

function onShow_2(){
}

function Popups(){
}

_ = Popups.prototype = new Sink();
_.onClick_0 = onClick_0;
_.onShow = onShow_2;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Popups';
_.typeId$ = 26;
function $Popups$1(this$static, $anonymous0, $anonymous1){
  $Sink$SinkInfo(this$static, $anonymous0, $anonymous1);
  return this$static;
}

function createInstance_2(){
  return $Popups(new Popups());
}

function getColor_0(){
  return '#bf2a2a';
}

function Popups$1(){
}

_ = Popups$1.prototype = new Sink$SinkInfo();
_.createInstance = createInstance_2;
_.getColor = getColor_0;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Popups$1';
_.typeId$ = 27;
function $adopt(this$static, child){
  $setParent(child, this$static);
}

function $orphan(this$static, child){
  $setParent(child, null);
}

function doAttachChildren(){
  var child, it;
  for (it = this.iterator_0(); it.hasNext();) {
    child = dynamicCast(it.next_0(), 15);
    child.onAttach();
  }
}

function doDetachChildren(){
  var child, it;
  for (it = this.iterator_0(); it.hasNext();) {
    child = dynamicCast(it.next_0(), 15);
    child.onDetach();
  }
}

function onLoad_1(){
}

function onUnload_1(){
}

function Panel(){
}

_ = Panel.prototype = new Widget();
_.doAttachChildren = doAttachChildren;
_.doDetachChildren = doDetachChildren;
_.onLoad = onLoad_1;
_.onUnload = onUnload_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Panel';
_.typeId$ = 28;
function $SimplePanel(this$static){
  $SimplePanel_0(this$static, createDiv());
  return this$static;
}

function $SimplePanel_0(this$static, elem){
  this$static.setElement(elem);
  return this$static;
}

function $setWidget_2(this$static, w){
  if (w === this$static.widget) {
    return;
  }
  if (w !== null) {
    $removeFromParent(w);
  }
  if (this$static.widget !== null) {
    this$static.remove_0(this$static.widget);
  }
  this$static.widget = w;
  if (w !== null) {
    appendChild(this$static.getContainerElement(), this$static.widget.getElement());
    $adopt(this$static, w);
  }
}

function getContainerElement_0(){
  return this.getElement();
}

function iterator_3(){
  return $SimplePanel$1(new SimplePanel$1(), this);
}

function remove_8(w){
  if (this.widget !== w) {
    return false;
  }
  $orphan(this, w);
  removeChild(this.getContainerElement(), w.getElement());
  this.widget = null;
  return true;
}

function setWidget_1(w){
  $setWidget_2(this, w);
}

function SimplePanel(){
}

_ = SimplePanel.prototype = new Panel();
_.getContainerElement = getContainerElement_0;
_.iterator_0 = iterator_3;
_.remove_0 = remove_8;
_.setWidget = setWidget_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SimplePanel';
_.typeId$ = 29;
_.widget = null;
function $clinit_149(){
  $clinit_149 = nullMethod;
  impl_4 = new PopupImpl();
}

function $PopupPanel(this$static){
  $clinit_149();
  $SimplePanel_0(this$static, $createElement_0(impl_4));
  $setPopupPosition(this$static, 0, 0);
  return this$static;
}

function $PopupPanel_0(this$static, autoHide){
  $clinit_149();
  $PopupPanel(this$static);
  this$static.autoHide = autoHide;
  return this$static;
}

function $PopupPanel_1(this$static, autoHide, modal){
  $clinit_149();
  $PopupPanel_0(this$static, autoHide);
  this$static.modal = modal;
  return this$static;
}

function $addPopupListener(this$static, listener){
  if (this$static.popupListeners === null) {
    this$static.popupListeners = $PopupListenerCollection(new PopupListenerCollection());
  }
  $add_9(this$static.popupListeners, listener);
}

function $blur(this$static, elt){
  if (elt.blur) {
    elt.blur();
  }
}

function $center(this$static){
  var initiallyShowing, left, top;
  initiallyShowing = this$static.showing;
  if (!initiallyShowing) {
    $setVisible(this$static, false);
    $show_0(this$static);
  }
  left = round_int((getClientWidth() - $getOffsetWidth(this$static)) / 2);
  top = round_int((getClientHeight() - $getOffsetHeight(this$static)) / 2);
  $setPopupPosition(this$static, getScrollLeft() + left, getScrollTop() + top);
  if (!initiallyShowing) {
    $setVisible(this$static, true);
  }
}

function $getContainerElement(this$static){
  return this$static.getElement();
}

function $getOffsetHeight(this$static){
  return $getOffsetHeight_0(this$static);
}

function $getOffsetWidth(this$static){
  return $getOffsetWidth_0(this$static);
}

function $hide(this$static){
  $hide_0(this$static, false);
}

function $hide_0(this$static, autoClosed){
  if (!this$static.showing) {
    return;
  }
  this$static.showing = false;
  $remove_0(get(), this$static);
  this$static.getElement();
  if (this$static.popupListeners !== null) {
    $firePopupClosed(this$static.popupListeners, this$static, autoClosed);
  }
}

function $maybeUpdateSize(this$static){
  var w;
  w = this$static.widget;
  if (w !== null) {
    if (this$static.desiredHeight !== null) {
      w.setHeight(this$static.desiredHeight);
    }
    if (this$static.desiredWidth !== null) {
      w.setWidth(this$static.desiredWidth);
    }
  }
}

function $onEventPreview(this$static, event_0){
  var allow, eventTargetsPopup, target, type;
  target = eventGetTarget(event_0);
  eventTargetsPopup = isOrHasChild(this$static.getElement(), target);
  type = eventGetType(event_0);
  switch (type) {
    case 128:
      {
        allow = (narrow_char(eventGetKeyCode(event_0)) , getKeyboardModifiers(event_0) , true);
        return allow && (eventTargetsPopup || !this$static.modal);
      }

    case 512:
      {
        allow = (narrow_char(eventGetKeyCode(event_0)) , getKeyboardModifiers(event_0) , true);
        return allow && (eventTargetsPopup || !this$static.modal);
      }

    case 256:
      {
        allow = (narrow_char(eventGetKeyCode(event_0)) , getKeyboardModifiers(event_0) , true);
        return allow && (eventTargetsPopup || !this$static.modal);
      }

    case 4:
    case 8:
    case 64:
    case 1:
    case 2:
      {
        if (($clinit_41() , sCaptureElem) !== null) {
          return true;
        }
        if (!eventTargetsPopup && this$static.autoHide && type == 4) {
          $hide_0(this$static, true);
          return true;
        }
        break;
      }

    case 2048:
      {
        if (this$static.modal && !eventTargetsPopup && target !== null) {
          $blur(this$static, target);
          return false;
        }
      }

  }
  return !this$static.modal || eventTargetsPopup;
}

function $setPopupPosition(this$static, left, top){
  var elem;
  if (left < 0) {
    left = 0;
  }
  if (top < 0) {
    top = 0;
  }
  this$static.leftPosition = left;
  this$static.topPosition = top;
  elem = this$static.getElement();
  setStyleAttribute(elem, 'left', left + 'px');
  setStyleAttribute(elem, 'top', top + 'px');
}

function $setPopupPositionAndShow(this$static, callback){
  $setVisible(this$static, false);
  $show_0(this$static);
  $setPosition(callback, $getOffsetWidth(this$static), $getOffsetHeight(this$static));
  $setVisible(this$static, true);
}

function $setVisible(this$static, visible){
  setStyleAttribute(this$static.getElement(), 'visibility', visible?'visible':'hidden');
  this$static.getElement();
}

function $setWidget_1(this$static, w){
  $setWidget_2(this$static, w);
  $maybeUpdateSize(this$static);
}

function $setWidth_0(this$static, width){
  this$static.desiredWidth = width;
  $maybeUpdateSize(this$static);
  if ($length(width) == 0) {
    this$static.desiredWidth = null;
  }
}

function $show_0(this$static){
  if (this$static.showing) {
    return;
  }
  this$static.showing = true;
  addEventPreview(this$static);
  setStyleAttribute(this$static.getElement(), 'position', 'absolute');
  if (this$static.topPosition != (-1)) {
    $setPopupPosition(this$static, this$static.leftPosition, this$static.topPosition);
  }
  $add(get(), this$static);
  this$static.getElement();
}

function getContainerElement(){
  return $getContainerElement(this);
}

function getOffsetHeight(){
  return $getOffsetHeight(this);
}

function getOffsetWidth(){
  return $getOffsetWidth(this);
}

function getStyleElement(){
  return this.getElement();
}

function onDetach_2(){
  removeEventPreview(this);
  $onDetach(this);
}

function onEventPreview_1(event_0){
  return $onEventPreview(this, event_0);
}

function setHeight(height){
  this.desiredHeight = height;
  $maybeUpdateSize(this);
  if ($length(height) == 0) {
    this.desiredHeight = null;
  }
}

function setTitle(title){
  var containerElement;
  containerElement = $getContainerElement(this);
  if (title === null || $length(title) == 0) {
    removeElementAttribute(containerElement, 'title');
  }
   else {
    setElementAttribute(containerElement, 'title', title);
  }
}

function setVisible(visible){
  $setVisible(this, visible);
}

function setWidget_0(w){
  $setWidget_1(this, w);
}

function setWidth_0(width){
  $setWidth_0(this, width);
}

function PopupPanel(){
}

_ = PopupPanel.prototype = new SimplePanel();
_.getContainerElement = getContainerElement;
_.getOffsetHeight = getOffsetHeight;
_.getOffsetWidth = getOffsetWidth;
_.getStyleElement = getStyleElement;
_.onDetach = onDetach_2;
_.onEventPreview = onEventPreview_1;
_.setHeight = setHeight;
_.setTitle = setTitle;
_.setVisible = setVisible;
_.setWidget = setWidget_0;
_.setWidth = setWidth_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'PopupPanel';
_.typeId$ = 30;
_.autoHide = false;
_.desiredHeight = null;
_.desiredWidth = null;
_.leftPosition = (-1);
_.modal = false;
_.popupListeners = null;
_.showing = false;
_.topPosition = (-1);
var impl_4;
function $clinit_76(){
  $clinit_76 = nullMethod;
  $clinit_149();
}

function $$init_8(this$static){
  this$static.caption = $HTML(new HTML());
  this$static.panel = $FlexTable(new FlexTable());
}

function $DialogBox(this$static){
  $clinit_76();
  $DialogBox_0(this$static, false);
  return this$static;
}

function $DialogBox_0(this$static, autoHide){
  $clinit_76();
  $DialogBox_1(this$static, autoHide, true);
  return this$static;
}

function $DialogBox_1(this$static, autoHide, modal){
  $clinit_76();
  $PopupPanel_1(this$static, autoHide, modal);
  $$init_8(this$static);
  $setWidget_0(this$static.panel, 0, 0, this$static.caption);
  this$static.panel.setHeight('100%');
  $setBorderWidth(this$static.panel, 0);
  $setCellPadding(this$static.panel, 0);
  $setCellSpacing(this$static.panel, 0);
  $setHeight(this$static.panel.cellFormatter, 1, 0, '100%');
  $setWidth(this$static.panel.cellFormatter, 1, 0, '100%');
  $setAlignment(this$static.panel.cellFormatter, 1, 0, ($clinit_108() , ALIGN_CENTER), ($clinit_112() , ALIGN_MIDDLE));
  $setWidget_1(this$static, this$static.panel);
  $setStyleName(this$static, 'gwt-DialogBox');
  $setStyleName(this$static.caption, 'Caption');
  $addMouseListener(this$static.caption, this$static);
  return this$static;
}

function $setText_0(this$static, text){
  $setText_3(this$static.caption, text);
}

function $setWidget(this$static, w){
  if (this$static.child !== null) {
    $remove_3(this$static.panel, this$static.child);
  }
  if (w !== null) {
    $setWidget_0(this$static.panel, 1, 0, w);
  }
  this$static.child = w;
}

function onEventPreview(event_0){
  if (eventGetType(event_0) == 4) {
    if (isOrHasChild(this.caption.getElement(), eventGetTarget(event_0))) {
      eventPreventDefault(event_0);
    }
  }
  return $onEventPreview(this, event_0);
}

function onMouseDown(sender, x, y){
  this.dragging = true;
  setCapture(this.caption.getElement());
  this.dragStartX = x;
  this.dragStartY = y;
}

function onMouseEnter(sender){
}

function onMouseLeave(sender){
}

function onMouseMove(sender, x, y){
  var absX, absY;
  if (this.dragging) {
    absX = x + $getAbsoluteLeft_0(this);
    absY = y + $getAbsoluteTop_0(this);
    $setPopupPosition(this, absX - this.dragStartX, absY - this.dragStartY);
  }
}

function onMouseUp(sender, x, y){
  this.dragging = false;
  releaseCapture(this.caption.getElement());
}

function remove_3(w){
  if (this.child !== w) {
    return false;
  }
  $remove_3(this.panel, w);
  return true;
}

function setWidget(w){
  $setWidget(this, w);
}

function setWidth(width){
  $setWidth_0(this, width);
  this.panel.setWidth('100%');
}

function DialogBox(){
}

_ = DialogBox.prototype = new PopupPanel();
_.onEventPreview = onEventPreview;
_.onMouseDown = onMouseDown;
_.onMouseEnter = onMouseEnter;
_.onMouseLeave = onMouseLeave;
_.onMouseMove = onMouseMove;
_.onMouseUp = onMouseUp;
_.remove_0 = remove_3;
_.setWidget = setWidget;
_.setWidth = setWidth;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DialogBox';
_.typeId$ = 31;
_.child = null;
_.dragStartX = 0;
_.dragStartY = 0;
_.dragging = false;
function $clinit_16(){
  $clinit_16 = nullMethod;
  $clinit_76();
}

function $Popups$MyDialog(this$static){
  var closeButton, dock, msg;
  $clinit_16();
  $DialogBox(this$static);
  $setText_0(this$static, 'Sample DialogBox');
  closeButton = $Button_1(new Button(), 'Close', this$static);
  msg = $HTML_1(new HTML(), '<center>This is an example of a standard dialog box component.<\/center>', true);
  dock = $DockPanel(new DockPanel());
  $setSpacing(dock, 4);
  $add_1(dock, closeButton, ($clinit_87() , SOUTH));
  $add_1(dock, msg, ($clinit_87() , NORTH));
  $add_1(dock, $Image_0(new Image_0(), 'images/jimmy.jpg'), ($clinit_87() , CENTER));
  $setCellHorizontalAlignment_0(dock, closeButton, ($clinit_108() , ALIGN_RIGHT));
  dock.setWidth('100%');
  $setWidget(this$static, dock);
  return this$static;
}

function onClick(sender){
  $hide(this);
}

function Popups$MyDialog(){
}

_ = Popups$MyDialog.prototype = new DialogBox();
_.onClick_0 = onClick;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Popups$MyDialog';
_.typeId$ = 32;
function $clinit_17(){
  $clinit_17 = nullMethod;
  $clinit_149();
}

function $Popups$MyPopup(this$static){
  var contents;
  $clinit_17();
  $PopupPanel_0(this$static, true);
  contents = $HTML_0(new HTML(), 'Click anywhere outside this popup to make it disappear.');
  contents.setWidth('128px');
  this$static.setWidget(contents);
  $setStyleName(this$static, 'ks-popups-Popup');
  return this$static;
}

function Popups$MyPopup(){
}

_ = Popups$MyPopup.prototype = new PopupPanel();
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Popups$MyPopup';
_.typeId$ = 33;
function $clinit_21(){
  $clinit_21 = nullMethod;
  fontSizesConstants = initValues('[Lcom.google.gwt.user.client.ui.RichTextArea$FontSize;', 209, 39, [($clinit_156() , XX_SMALL), ($clinit_156() , X_SMALL), ($clinit_156() , SMALL), ($clinit_156() , MEDIUM), ($clinit_156() , LARGE), ($clinit_156() , X_LARGE), ($clinit_156() , XX_LARGE)]);
}

function $$init_2(this$static){
  $RichTextToolbar_Images_generatedBundle(new RichTextToolbar_Images_generatedBundle());
  this$static.listener = $RichTextToolbar$EventListener(new RichTextToolbar$EventListener(), this$static);
  this$static.outer = $VerticalPanel(new VerticalPanel());
  this$static.topPanel = $HorizontalPanel(new HorizontalPanel());
  this$static.bottomPanel = $HorizontalPanel(new HorizontalPanel());
}

function $RichTextToolbar(this$static, richText){
  $clinit_21();
  $$init_2(this$static);
  this$static.richText = richText;
  this$static.basic = $getBasicFormatter(richText);
  this$static.extended = $getExtendedFormatter(richText);
  $add_7(this$static.outer, this$static.topPanel);
  $add_7(this$static.outer, this$static.bottomPanel);
  this$static.topPanel.setWidth('100%');
  this$static.bottomPanel.setWidth('100%');
  $initWidget(this$static, this$static.outer);
  $setStyleName(this$static, 'gwt-RichTextToolbar');
  if (this$static.basic !== null) {
    $add_3(this$static.topPanel, this$static.bold = $createToggleButton(this$static, ($clinit_22() , bold_SINGLETON), 'Toggle Bold'));
    $add_3(this$static.topPanel, this$static.italic = $createToggleButton(this$static, ($clinit_22() , italic_SINGLETON), 'Toggle Italic'));
    $add_3(this$static.topPanel, this$static.underline = $createToggleButton(this$static, ($clinit_22() , underline_SINGLETON), 'Toggle Underline'));
    $add_3(this$static.topPanel, this$static.subscript = $createToggleButton(this$static, ($clinit_22() , subscript_SINGLETON), 'Toggle Subscript'));
    $add_3(this$static.topPanel, this$static.superscript = $createToggleButton(this$static, ($clinit_22() , superscript_SINGLETON), 'Toggle Superscript'));
    $add_3(this$static.topPanel, this$static.justifyLeft = $createPushButton(this$static, ($clinit_22() , justifyLeft_SINGLETON), 'Left Justify'));
    $add_3(this$static.topPanel, this$static.justifyCenter = $createPushButton(this$static, ($clinit_22() , justifyCenter_SINGLETON), 'Center'));
    $add_3(this$static.topPanel, this$static.justifyRight = $createPushButton(this$static, ($clinit_22() , justifyRight_SINGLETON), 'Right Justify'));
  }
  if (this$static.extended !== null) {
    $add_3(this$static.topPanel, this$static.strikethrough = $createToggleButton(this$static, ($clinit_22() , strikeThrough_SINGLETON), 'Toggle Strikethrough'));
    $add_3(this$static.topPanel, this$static.indent = $createPushButton(this$static, ($clinit_22() , indent_SINGLETON), 'Indent Right'));
    $add_3(this$static.topPanel, this$static.outdent = $createPushButton(this$static, ($clinit_22() , outdent_SINGLETON), 'Indent Left'));
    $add_3(this$static.topPanel, this$static.hr = $createPushButton(this$static, ($clinit_22() , hr_SINGLETON), 'Insert Horizontal Rule'));
    $add_3(this$static.topPanel, this$static.ol = $createPushButton(this$static, ($clinit_22() , ol_SINGLETON), 'Insert Ordered List'));
    $add_3(this$static.topPanel, this$static.ul = $createPushButton(this$static, ($clinit_22() , ul_SINGLETON), 'Insert Unordered List'));
    $add_3(this$static.topPanel, this$static.insertImage = $createPushButton(this$static, ($clinit_22() , insertImage_SINGLETON), 'Insert Image'));
    $add_3(this$static.topPanel, this$static.createLink = $createPushButton(this$static, ($clinit_22() , createLink_SINGLETON), 'Create Link'));
    $add_3(this$static.topPanel, this$static.removeLink = $createPushButton(this$static, ($clinit_22() , removeLink_SINGLETON), 'Remove Link'));
    $add_3(this$static.topPanel, this$static.removeFormat = $createPushButton(this$static, ($clinit_22() , removeFormat_SINGLETON), 'Remove Formatting'));
  }
  if (this$static.basic !== null) {
    $add_3(this$static.bottomPanel, this$static.backColors = $createColorList(this$static, 'Background'));
    $add_3(this$static.bottomPanel, this$static.foreColors = $createColorList(this$static, 'Foreground'));
    $add_3(this$static.bottomPanel, this$static.fonts = $createFontList(this$static));
    $add_3(this$static.bottomPanel, this$static.fontSizes = $createFontSizes(this$static));
    richText.addKeyboardListener(this$static.listener);
    richText.addClickListener(this$static.listener);
  }
  return this$static;
}

function $createColorList(this$static, caption){
  var lb;
  lb = $ListBox(new ListBox());
  $addChangeListener(lb, this$static.listener);
  $setVisibleItemCount(lb, 1);
  $addItem(lb, caption);
  $addItem_0(lb, 'White', 'white');
  $addItem_0(lb, 'Black', 'black');
  $addItem_0(lb, 'Red', 'red');
  $addItem_0(lb, 'Green', 'green');
  $addItem_0(lb, 'Yellow', 'yellow');
  $addItem_0(lb, 'Blue', 'blue');
  return lb;
}

function $createFontList(this$static){
  var lb;
  lb = $ListBox(new ListBox());
  $addChangeListener(lb, this$static.listener);
  $setVisibleItemCount(lb, 1);
  $addItem_0(lb, 'Font', '');
  $addItem_0(lb, 'Normal', '');
  $addItem_0(lb, 'Times New Roman', 'Times New Roman');
  $addItem_0(lb, 'Arial', 'Arial');
  $addItem_0(lb, 'Courier New', 'Courier New');
  $addItem_0(lb, 'Georgia', 'Georgia');
  $addItem_0(lb, 'Trebuchet', 'Trebuchet');
  $addItem_0(lb, 'Verdana', 'Verdana');
  return lb;
}

function $createFontSizes(this$static){
  var lb;
  lb = $ListBox(new ListBox());
  $addChangeListener(lb, this$static.listener);
  $setVisibleItemCount(lb, 1);
  $addItem(lb, 'Size');
  $addItem(lb, 'XX-Small');
  $addItem(lb, 'X-Small');
  $addItem(lb, 'Small');
  $addItem(lb, 'Medium');
  $addItem(lb, 'Large');
  $addItem(lb, 'X-Large');
  $addItem(lb, 'XX-Large');
  return lb;
}

function $createPushButton(this$static, img, tip){
  var pb;
  pb = $PushButton(new PushButton(), $createImage(img));
  pb.addClickListener(this$static.listener);
  pb.setTitle(tip);
  return pb;
}

function $createToggleButton(this$static, img, tip){
  var tb;
  tb = $ToggleButton(new ToggleButton(), $createImage(img));
  tb.addClickListener(this$static.listener);
  tb.setTitle(tip);
  return tb;
}

function $updateStatus(this$static){
  if (this$static.basic !== null) {
    $setDown_0(this$static.bold, $isBold(this$static.basic));
    $setDown_0(this$static.italic, $isItalic(this$static.basic));
    $setDown_0(this$static.underline, $isUnderlined(this$static.basic));
    $setDown_0(this$static.subscript, $isSubscript(this$static.basic));
    $setDown_0(this$static.superscript, $isSuperscript(this$static.basic));
  }
  if (this$static.extended !== null) {
    $setDown_0(this$static.strikethrough, $isStrikethrough(this$static.extended));
  }
}

function RichTextToolbar(){
}

_ = RichTextToolbar.prototype = new Composite();
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'RichTextToolbar';
_.typeId$ = 34;
_.backColors = null;
_.basic = null;
_.bold = null;
_.createLink = null;
_.extended = null;
_.fontSizes = null;
_.fonts = null;
_.foreColors = null;
_.hr = null;
_.indent = null;
_.insertImage = null;
_.italic = null;
_.justifyCenter = null;
_.justifyLeft = null;
_.justifyRight = null;
_.ol = null;
_.outdent = null;
_.removeFormat = null;
_.removeLink = null;
_.richText = null;
_.strikethrough = null;
_.subscript = null;
_.superscript = null;
_.ul = null;
_.underline = null;
var fontSizesConstants;
function $RichTextToolbar$EventListener(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function onChange_0(sender){
  if (sender === this.this$0.backColors) {
    $setBackColor(this.this$0.basic, $getValue(this.this$0.backColors, $getSelectedIndex(this.this$0.backColors)));
    $setSelectedIndex(this.this$0.backColors, 0);
  }
   else if (sender === this.this$0.foreColors) {
    $setForeColor(this.this$0.basic, $getValue(this.this$0.foreColors, $getSelectedIndex(this.this$0.foreColors)));
    $setSelectedIndex(this.this$0.foreColors, 0);
  }
   else if (sender === this.this$0.fonts) {
    $setFontName(this.this$0.basic, $getValue(this.this$0.fonts, $getSelectedIndex(this.this$0.fonts)));
    $setSelectedIndex(this.this$0.fonts, 0);
  }
   else if (sender === this.this$0.fontSizes) {
    $setFontSize(this.this$0.basic, ($clinit_21() , fontSizesConstants)[$getSelectedIndex(this.this$0.fontSizes) - 1]);
    $setSelectedIndex(this.this$0.fontSizes, 0);
  }
}

function onClick_1(sender){
  var url;
  if (sender === this.this$0.bold) {
    $toggleBold(this.this$0.basic);
  }
   else if (sender === this.this$0.italic) {
    $toggleItalic(this.this$0.basic);
  }
   else if (sender === this.this$0.underline) {
    $toggleUnderline(this.this$0.basic);
  }
   else if (sender === this.this$0.subscript) {
    $toggleSubscript(this.this$0.basic);
  }
   else if (sender === this.this$0.superscript) {
    $toggleSuperscript(this.this$0.basic);
  }
   else if (sender === this.this$0.strikethrough) {
    $toggleStrikethrough(this.this$0.extended);
  }
   else if (sender === this.this$0.indent) {
    $rightIndent(this.this$0.extended);
  }
   else if (sender === this.this$0.outdent) {
    $leftIndent(this.this$0.extended);
  }
   else if (sender === this.this$0.justifyLeft) {
    $setJustification(this.this$0.basic, ($clinit_157() , LEFT));
  }
   else if (sender === this.this$0.justifyCenter) {
    $setJustification(this.this$0.basic, ($clinit_157() , CENTER_0));
  }
   else if (sender === this.this$0.justifyRight) {
    $setJustification(this.this$0.basic, ($clinit_157() , RIGHT));
  }
   else if (sender === this.this$0.insertImage) {
    url = prompt('Enter an image URL:', 'http://');
    if (url !== null) {
      $insertImage(this.this$0.extended, url);
    }
  }
   else if (sender === this.this$0.createLink) {
    url = prompt('Enter a link URL:', 'http://');
    if (url !== null) {
      $createLink(this.this$0.extended, url);
    }
  }
   else if (sender === this.this$0.removeLink) {
    $removeLink(this.this$0.extended);
  }
   else if (sender === this.this$0.hr) {
    $insertHorizontalRule(this.this$0.extended);
  }
   else if (sender === this.this$0.ol) {
    $insertOrderedList(this.this$0.extended);
  }
   else if (sender === this.this$0.ul) {
    $insertUnorderedList(this.this$0.extended);
  }
   else if (sender === this.this$0.removeFormat) {
    $removeFormat(this.this$0.extended);
  }
   else if (sender === this.this$0.richText) {
    $updateStatus(this.this$0);
  }
}

function onKeyDown(sender, keyCode, modifiers){
}

function onKeyPress(sender, keyCode, modifiers){
}

function onKeyUp(sender, keyCode, modifiers){
  if (sender === this.this$0.richText) {
    $updateStatus(this.this$0);
  }
}

function RichTextToolbar$EventListener(){
}

_ = RichTextToolbar$EventListener.prototype = new Object_0();
_.onChange = onChange_0;
_.onClick_0 = onClick_1;
_.onKeyDown = onKeyDown;
_.onKeyPress = onKeyPress;
_.onKeyUp = onKeyUp;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'RichTextToolbar$EventListener';
_.typeId$ = 35;
function $clinit_22(){
  $clinit_22 = nullMethod;
  IMAGE_BUNDLE_URL = getModuleBaseURL() + 'DD7A9D3C7EA0FB9E38F34F92B31BF6AE.cache.png';
  bold_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 0, 0, 20, 20);
  createLink_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 20, 0, 20, 20);
  hr_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 40, 0, 20, 20);
  indent_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 60, 0, 20, 20);
  insertImage_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 80, 0, 20, 20);
  italic_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 100, 0, 20, 20);
  justifyCenter_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 120, 0, 20, 20);
  justifyLeft_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 140, 0, 20, 20);
  justifyRight_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 160, 0, 20, 20);
  ol_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 180, 0, 20, 20);
  outdent_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 200, 0, 20, 20);
  removeFormat_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 220, 0, 20, 20);
  removeLink_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 240, 0, 20, 20);
  strikeThrough_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 260, 0, 20, 20);
  subscript_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 280, 0, 20, 20);
  superscript_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 300, 0, 20, 20);
  ul_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 320, 0, 20, 20);
  underline_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 340, 0, 20, 20);
}

function $RichTextToolbar_Images_generatedBundle(this$static){
  $clinit_22();
  return this$static;
}

function RichTextToolbar_Images_generatedBundle(){
}

_ = RichTextToolbar_Images_generatedBundle.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'RichTextToolbar_Images_generatedBundle';
_.typeId$ = 36;
var IMAGE_BUNDLE_URL, bold_SINGLETON, createLink_SINGLETON, hr_SINGLETON, indent_SINGLETON, insertImage_SINGLETON, italic_SINGLETON, justifyCenter_SINGLETON, justifyLeft_SINGLETON, justifyRight_SINGLETON, ol_SINGLETON, outdent_SINGLETON, removeFormat_SINGLETON, removeLink_SINGLETON, strikeThrough_SINGLETON, subscript_SINGLETON, superscript_SINGLETON, ul_SINGLETON, underline_SINGLETON;
function $$init_3(this$static){
  this$static.list = $HorizontalPanel(new HorizontalPanel());
  this$static.sinks = $ArrayList(new ArrayList());
}

function $SinkList(this$static, images){
  $$init_3(this$static);
  $initWidget(this$static, this$static.list);
  $add_3(this$static.list, $createImage(($clinit_28() , gwtLogo_SINGLETON)));
  $setStyleName(this$static, 'ks-List');
  return this$static;
}

function $addSink(this$static, info){
  var index, link, name;
  name = info.name_0;
  index = this$static.list.children_0.size - 1;
  link = $SinkList$MouseLink(new SinkList$MouseLink(), name, index, this$static);
  $add_3(this$static.list, link);
  $add_9(this$static.sinks, info);
  this$static.list.setCellVerticalAlignment(link, ($clinit_112() , ALIGN_BOTTOM));
  $styleSink(this$static, index, false);
}

function $colorSink(this$static, index, on){
  var color, w;
  color = '';
  if (on) {
    color = dynamicCast($get_0(this$static.sinks, index), 5).getColor();
  }
  w = $getWidget(this$static.list, index + 1);
  setStyleAttribute(w.getElement(), 'backgroundColor', color);
}

function $find(this$static, sinkName){
  var i, info;
  for (i = 0; i < this$static.sinks.size; ++i) {
    info = dynamicCast($get_0(this$static.sinks, i), 5);
    if ($equals_1(info.name_0, sinkName)) {
      return info;
    }
  }
  return null;
}

function $mouseOut(this$static, index){
  if (index != this$static.selectedSink) {
    $colorSink(this$static, index, false);
  }
}

function $mouseOver(this$static, index){
  if (index != this$static.selectedSink) {
    $colorSink(this$static, index, true);
  }
}

function $setSinkSelection(this$static, name){
  var i, info;
  if (this$static.selectedSink != (-1)) {
    $styleSink(this$static, this$static.selectedSink, false);
  }
  for (i = 0; i < this$static.sinks.size; ++i) {
    info = dynamicCast($get_0(this$static.sinks, i), 5);
    if ($equals_1(info.name_0, name)) {
      this$static.selectedSink = i;
      $styleSink(this$static, this$static.selectedSink, true);
      return;
    }
  }
}

function $styleSink(this$static, index, selected){
  var style, w;
  style = index == 0?'ks-FirstSinkItem':'ks-SinkItem';
  if (selected) {
    style += '-selected';
  }
  w = $getWidget(this$static.list, index + 1);
  $setStyleName(w, style);
  $colorSink(this$static, index, selected);
}

function SinkList(){
}

_ = SinkList.prototype = new Composite();
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'SinkList';
_.typeId$ = 37;
_.selectedSink = (-1);
function $Hyperlink(this$static){
  this$static.setElement(createDiv());
  appendChild(this$static.getElement(), this$static.anchorElem = createAnchor());
  $sinkEvents_0(this$static, 1);
  $setStyleName(this$static, 'gwt-Hyperlink');
  return this$static;
}

function $Hyperlink_0(this$static, text, targetHistoryToken){
  $Hyperlink(this$static);
  $setText_2(this$static, text);
  $setTargetHistoryToken(this$static, targetHistoryToken);
  return this$static;
}

function $onBrowserEvent_0(this$static, event_0){
  if (eventGetType(event_0) == 1) {
    newItem(this$static.targetHistoryToken);
    eventPreventDefault(event_0);
  }
}

function $setTargetHistoryToken(this$static, targetHistoryToken){
  this$static.targetHistoryToken = targetHistoryToken;
  setElementProperty(this$static.anchorElem, 'href', '#' + targetHistoryToken);
}

function $setText_2(this$static, text){
  setInnerText(this$static.anchorElem, text);
}

function onBrowserEvent_4(event_0){
  $onBrowserEvent_0(this, event_0);
}

function Hyperlink(){
}

_ = Hyperlink.prototype = new Widget();
_.onBrowserEvent = onBrowserEvent_4;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Hyperlink';
_.typeId$ = 38;
_.anchorElem = null;
_.targetHistoryToken = null;
function $SinkList$MouseLink(this$static, name, index, this$0){
  this$static.this$0 = this$0;
  $Hyperlink_0(this$static, name, name);
  this$static.index_0 = index;
  $sinkEvents_0(this$static, 124);
  return this$static;
}

function onBrowserEvent(event_0){
  switch (eventGetType(event_0)) {
    case 16:
      $mouseOver(this.this$0, this.index_0);
      break;
    case 32:
      $mouseOut(this.this$0, this.index_0);
      break;
  }
  $onBrowserEvent_0(this, event_0);
}

function SinkList$MouseLink(){
}

_ = SinkList$MouseLink.prototype = new Hyperlink();
_.onBrowserEvent = onBrowserEvent;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'SinkList$MouseLink';
_.typeId$ = 39;
_.index_0 = 0;
function $clinit_28(){
  $clinit_28 = nullMethod;
  IMAGE_BUNDLE_URL_0 = getModuleBaseURL() + '127C1F9EB6FF2DFA33DBDB7ADB62C029.cache.png';
  gwtLogo_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL_0, 0, 0, 91, 75);
  treeClosed_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL_0, 91, 0, 16, 16);
  treeLeaf_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL_0, 107, 0, 16, 16);
  treeOpen_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL_0, 123, 0, 16, 16);
}

function $Sink_Images_generatedBundle(this$static){
  $clinit_28();
  return this$static;
}

function Sink_Images_generatedBundle(){
}

_ = Sink_Images_generatedBundle.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Sink_Images_generatedBundle';
_.typeId$ = 40;
var IMAGE_BUNDLE_URL_0, gwtLogo_SINGLETON, treeClosed_SINGLETON, treeLeaf_SINGLETON, treeOpen_SINGLETON;
function $$init_4(this$static){
  this$static.passwordText = $PasswordTextBox(new PasswordTextBox());
  this$static.textArea = $TextArea(new TextArea());
  this$static.textBox = $TextBox(new TextBox());
}

function $Text(this$static){
  var hp, readOnlyTextBox, richText, vp;
  $$init_4(this$static);
  readOnlyTextBox = $TextBox(new TextBox());
  $setReadOnly(readOnlyTextBox, true);
  $setText_5(readOnlyTextBox, 'read only');
  vp = $VerticalPanel(new VerticalPanel());
  $setSpacing(vp, 8);
  $add_7(vp, $HTML_0(new HTML(), 'Normal text box:'));
  $add_7(vp, $createTextThing(this$static, this$static.textBox, true));
  $add_7(vp, $createTextThing(this$static, readOnlyTextBox, false));
  $add_7(vp, $HTML_0(new HTML(), 'Password text box:'));
  $add_7(vp, $createTextThing(this$static, this$static.passwordText, true));
  $add_7(vp, $HTML_0(new HTML(), 'Text area:'));
  $add_7(vp, $createTextThing(this$static, this$static.textArea, true));
  $setVisibleLines(this$static.textArea, 5);
  richText = $createRichText(this$static);
  richText.setWidth('32em');
  hp = $HorizontalPanel(new HorizontalPanel());
  $add_3(hp, vp);
  $add_3(hp, richText);
  hp.setCellHorizontalAlignment(vp, ($clinit_108() , ALIGN_LEFT));
  hp.setCellHorizontalAlignment(richText, ($clinit_108() , ALIGN_RIGHT));
  $initWidget(this$static, hp);
  hp.setWidth('100%');
  return this$static;
}

function $createRichText(this$static){
  var area, p, tb;
  area = $RichTextArea(new RichTextArea());
  tb = $RichTextToolbar(new RichTextToolbar(), area);
  p = $VerticalPanel(new VerticalPanel());
  $add_7(p, tb);
  $add_7(p, area);
  area.setHeight('14em');
  area.setWidth('100%');
  tb.setWidth('100%');
  p.setWidth('100%');
  setStyleAttribute(p.getElement(), 'margin-right', '4px');
  return p;
}

function $createTextThing(this$static, textBox, addSelection){
  var echo, p;
  p = $HorizontalPanel(new HorizontalPanel());
  $setSpacing(p, 4);
  textBox.setWidth('20em');
  $add_3(p, textBox);
  if (addSelection) {
    echo = $HTML(new HTML());
    $add_3(p, echo);
    $addKeyboardListener(textBox, $Text$2(new Text$2(), this$static, textBox, echo));
    $addClickListener_0(textBox, $Text$3(new Text$3(), this$static, textBox, echo));
    $updateText(this$static, textBox, echo);
  }
  return p;
}

function $updateText(this$static, text, echo){
  $setHTML_0(echo, 'Selection: ' + text.getCursorPos() + ', ' + text.getSelectionLength());
}

function init_3(){
  return $Text$1(new Text$1(), 'Text', '<h2>Basic and Rich Text<\/h2><p>GWT includes the standard complement of text-entry widgets, each of which supports keyboard and selection events you can use to control text entry.  In particular, notice that the selection range for each widget is updated whenever you press a key.<\/p><p>Also notice the rich-text area to the right. This is supported on all major browsers, and will fall back gracefully to the level of functionality supported on each.<\/p>');
}

function onShow_4(){
}

function Text(){
}

_ = Text.prototype = new Sink();
_.onShow = onShow_4;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Text';
_.typeId$ = 41;
function $Text$1(this$static, $anonymous0, $anonymous1){
  $Sink$SinkInfo(this$static, $anonymous0, $anonymous1);
  return this$static;
}

function createInstance_3(){
  return $Text(new Text());
}

function getColor_2(){
  return '#2fba10';
}

function Text$1(){
}

_ = Text$1.prototype = new Sink$SinkInfo();
_.createInstance = createInstance_3;
_.getColor = getColor_2;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Text$1';
_.typeId$ = 42;
function onKeyDown_0(sender, keyCode, modifiers){
}

function onKeyPress_0(sender, keyCode, modifiers){
}

function onKeyUp_1(sender, keyCode, modifiers){
}

function KeyboardListenerAdapter(){
}

_ = KeyboardListenerAdapter.prototype = new Object_0();
_.onKeyDown = onKeyDown_0;
_.onKeyPress = onKeyPress_0;
_.onKeyUp = onKeyUp_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'KeyboardListenerAdapter';
_.typeId$ = 43;
function $Text$2(this$static, this$0, val$textBox, val$echo){
  this$static.this$0 = this$0;
  this$static.val$textBox = val$textBox;
  this$static.val$echo = val$echo;
  return this$static;
}

function onKeyUp_0(sender, keyCode, modifiers){
  $updateText(this.this$0, this.val$textBox, this.val$echo);
}

function Text$2(){
}

_ = Text$2.prototype = new KeyboardListenerAdapter();
_.onKeyUp = onKeyUp_0;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Text$2';
_.typeId$ = 44;
function $Text$3(this$static, this$0, val$textBox, val$echo){
  this$static.this$0 = this$0;
  this$static.val$textBox = val$textBox;
  this$static.val$echo = val$echo;
  return this$static;
}

function onClick_2(sender){
  $updateText(this.this$0, this.val$textBox, this.val$echo);
}

function Text$3(){
}

_ = Text$3.prototype = new Object_0();
_.onClick_0 = onClick_2;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Text$3';
_.typeId$ = 45;
function $$init_5(this$static){
  this$static.disabledButton = $Button_0(new Button(), 'Disabled Button');
  this$static.disabledCheck = $CheckBox_1(new CheckBox(), 'Disabled Check');
  this$static.normalButton = $Button_0(new Button(), 'Normal Button');
  this$static.normalCheck = $CheckBox_1(new CheckBox(), 'Normal Check');
  this$static.panel = $VerticalPanel(new VerticalPanel());
  this$static.radio0 = $RadioButton_0(new RadioButton(), 'group0', 'Choice 0');
  this$static.radio1 = $RadioButton_0(new RadioButton(), 'group0', 'Choice 1');
  this$static.radio2 = $RadioButton_0(new RadioButton(), 'group0', 'Choice 2 (Disabled)');
  this$static.radio3 = $RadioButton_0(new RadioButton(), 'group0', 'Choice 3');
}

function $Widgets(this$static, images){
  var hp;
  $$init_5(this$static);
  this$static.pushButton = $PushButton(new PushButton(), $createImage(($clinit_28() , gwtLogo_SINGLETON)));
  this$static.toggleButton = $ToggleButton(new ToggleButton(), $createImage(($clinit_28() , gwtLogo_SINGLETON)));
  $add_7(this$static.panel, $createMenu(this$static));
  $add_7(this$static.panel, hp = $HorizontalPanel(new HorizontalPanel()));
  $setSpacing(hp, 8);
  $add_3(hp, this$static.normalButton);
  $add_3(hp, this$static.disabledButton);
  $add_7(this$static.panel, hp = $HorizontalPanel(new HorizontalPanel()));
  $setSpacing(hp, 8);
  $add_3(hp, this$static.normalCheck);
  $add_3(hp, this$static.disabledCheck);
  $add_7(this$static.panel, hp = $HorizontalPanel(new HorizontalPanel()));
  $setSpacing(hp, 8);
  $add_3(hp, this$static.radio0);
  $add_3(hp, this$static.radio1);
  $add_3(hp, this$static.radio2);
  $add_3(hp, this$static.radio3);
  $add_7(this$static.panel, hp = $HorizontalPanel(new HorizontalPanel()));
  $setSpacing(hp, 8);
  $add_3(hp, this$static.pushButton);
  $add_3(hp, this$static.toggleButton);
  this$static.disabledButton.setEnabled(false);
  $setEnabled(this$static.disabledCheck, false);
  $setEnabled(this$static.radio2, false);
  $setSpacing(this$static.panel, 8);
  $initWidget(this$static, this$static.panel);
  return this$static;
}

function $createMenu(this$static){
  var menu, menu0, menu1, menu2, subMenu;
  menu = $MenuBar(new MenuBar());
  $setAutoOpen(menu, true);
  subMenu = $MenuBar_0(new MenuBar(), true);
  $addItem_3(subMenu, '<code>Code<\/code>', true, this$static);
  $addItem_3(subMenu, '<strike>Strikethrough<\/strike>', true, this$static);
  $addItem_3(subMenu, '<u>Underlined<\/u>', true, this$static);
  menu0 = $MenuBar_0(new MenuBar(), true);
  $addItem_3(menu0, '<b>Bold<\/b>', true, this$static);
  $addItem_3(menu0, '<i>Italicized<\/i>', true, this$static);
  $addItem_4(menu0, 'More &#187;', true, subMenu);
  menu1 = $MenuBar_0(new MenuBar(), true);
  $addItem_3(menu1, "<font color='#FF0000'><b>Apple<\/b><\/font>", true, this$static);
  $addItem_3(menu1, "<font color='#FFFF00'><b>Banana<\/b><\/font>", true, this$static);
  $addItem_3(menu1, "<font color='#FFFFFF'><b>Coconut<\/b><\/font>", true, this$static);
  $addItem_3(menu1, "<font color='#8B4513'><b>Donut<\/b><\/font>", true, this$static);
  menu2 = $MenuBar_0(new MenuBar(), true);
  $addItem_2(menu2, 'Bling', this$static);
  $addItem_2(menu2, 'Ginormous', this$static);
  $addItem_3(menu2, '<code>w00t!<\/code>', true, this$static);
  $addItem_1(menu, $MenuItem_0(new MenuItem(), 'Style', menu0));
  $addItem_1(menu, $MenuItem_0(new MenuItem(), 'Fruit', menu1));
  $addItem_1(menu, $MenuItem_0(new MenuItem(), 'Term', menu2));
  menu.setWidth('100%');
  return menu;
}

function execute(){
  alert('Thank you for selecting a menu item.');
}

function init_4(images){
  return $Widgets$1(new Widgets$1(), 'Widgets', '<h2>Basic Widgets<\/h2><p>GWT has all sorts of the basic widgets you would expect from any toolkit.<\/p><p>Below, you can see various kinds of buttons, check boxes, radio buttons, and menus.<\/p>', images);
}

function onShow_5(){
}

function Widgets(){
}

_ = Widgets.prototype = new Sink();
_.execute = execute;
_.onShow = onShow_5;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Widgets';
_.typeId$ = 46;
_.pushButton = null;
_.toggleButton = null;
function $Widgets$1(this$static, $anonymous0, $anonymous1, val$images){
  this$static.val$images = val$images;
  $Sink$SinkInfo(this$static, $anonymous0, $anonymous1);
  return this$static;
}

function createInstance_4(){
  return $Widgets(new Widgets(), this.val$images);
}

function getColor_3(){
  return '#bf2a2a';
}

function Widgets$1(){
}

_ = Widgets$1.prototype = new Sink$SinkInfo();
_.createInstance = createInstance_4;
_.getColor = getColor_3;
_.typeName$ = package_com_google_gwt_sample_kitchensink_client_ + 'Widgets$1';
_.typeId$ = 47;
function $Throwable(this$static, message){
  this$static.message = message;
  return this$static;
}

function toString_13(){
  var className, msg;
  className = getTypeName(this);
  msg = this.message;
  if (msg !== null) {
    return className + ': ' + msg;
  }
   else {
    return className;
  }
}

function Throwable(){
}

_ = Throwable.prototype = new Object_0();
_.toString$ = toString_13;
_.typeName$ = package_java_lang_ + 'Throwable';
_.typeId$ = 3;
_.message = null;
function $Exception(this$static, message){
  $Throwable(this$static, message);
  return this$static;
}

function Exception(){
}

_ = Exception.prototype = new Throwable();
_.typeName$ = package_java_lang_ + 'Exception';
_.typeId$ = 4;
function $RuntimeException(this$static, message){
  $Exception(this$static, message);
  return this$static;
}

function RuntimeException(){
}

_ = RuntimeException.prototype = new Exception();
_.typeName$ = package_java_lang_ + 'RuntimeException';
_.typeId$ = 5;
function $CommandCanceledException(this$static, command){
  return this$static;
}

function CommandCanceledException(){
}

_ = CommandCanceledException.prototype = new RuntimeException();
_.typeName$ = package_com_google_gwt_user_client_ + 'CommandCanceledException';
_.typeId$ = 48;
function $$init_6(this$static){
  this$static.cancellationTimer = $CommandExecutor$1(new CommandExecutor$1(), this$static);
  this$static.commands = $ArrayList(new ArrayList());
  this$static.executionTimer = $CommandExecutor$2(new CommandExecutor$2(), this$static);
  this$static.iterator = $CommandExecutor$CircularIterator(new CommandExecutor$CircularIterator(), this$static);
}

function $CommandExecutor(this$static){
  $$init_6(this$static);
  return this$static;
}

function $doCommandCanceled(this$static){
  var cmd, ex, ueh;
  cmd = $getLast(this$static.iterator);
  $remove(this$static.iterator);
  ex = null;
  if (instanceOf(cmd, 6)) {
    ex = $CommandCanceledException(new CommandCanceledException(), dynamicCast(cmd, 6));
  }
   else {
  }
  if (ex !== null) {
    ueh = sUncaughtExceptionHandler;
  }
  $setExecuting(this$static, false);
  $maybeStartExecutionTimer(this$static);
}

function $doExecuteCommands(this$static, startTimeMillis){
  var command, element, removeCommand, wasCanceled;
  wasCanceled = false;
  try {
    $setExecuting(this$static, true);
    $setEnd(this$static.iterator, this$static.commands.size);
    $schedule(this$static.cancellationTimer, 10000);
    while ($hasNext(this$static.iterator)) {
      element = $next(this$static.iterator);
      removeCommand = true;
      try {
        if (element === null) {
          return;
        }
        if (instanceOf(element, 6)) {
          command = dynamicCast(element, 6);
          command.execute();
        }
         else {
        }
      }
       finally {
        wasCanceled = $wasRemoved(this$static.iterator);
        if (wasCanceled) {
          return;
        }
        if (removeCommand) {
          $remove(this$static.iterator);
        }
      }
      if (hasTimeSliceExpired(currentTimeMillis_0(), startTimeMillis)) {
        return;
      }
    }
  }
   finally {
    if (!wasCanceled) {
      $cancel(this$static.cancellationTimer);
      $setExecuting(this$static, false);
      $maybeStartExecutionTimer(this$static);
    }
  }
}

function $maybeStartExecutionTimer(this$static){
  if (!$isEmpty(this$static.commands) && !this$static.executionTimerPending && !this$static.executing) {
    $setExecutionTimerPending(this$static, true);
    $schedule(this$static.executionTimer, 1);
  }
}

function $setExecuting(this$static, executing){
  this$static.executing = executing;
}

function $setExecutionTimerPending(this$static, pending){
  this$static.executionTimerPending = pending;
}

function $submit(this$static, command){
  $add_9(this$static.commands, command);
  $maybeStartExecutionTimer(this$static);
}

function hasTimeSliceExpired(currentTimeMillis, startTimeMillis){
  return abs(currentTimeMillis - startTimeMillis) >= 100;
}

function CommandExecutor(){
}

_ = CommandExecutor.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ + 'CommandExecutor';
_.typeId$ = 49;
_.executing = false;
_.executionTimerPending = false;
function $clinit_50(){
  $clinit_50 = nullMethod;
  timers = $ArrayList(new ArrayList());
  {
    hookWindowClosing();
  }
}

function $Timer(this$static){
  $clinit_50();
  return this$static;
}

function $cancel(this$static){
  if (this$static.isRepeating) {
    clearInterval(this$static.timerId);
  }
   else {
    clearTimeout(this$static.timerId);
  }
  $remove_13(timers, this$static);
}

function $fireImpl(this$static){
  if (!this$static.isRepeating) {
    $remove_13(timers, this$static);
  }
  this$static.run();
}

function $schedule(this$static, delayMillis){
  if (delayMillis <= 0) {
    throw $IllegalArgumentException(new IllegalArgumentException(), 'must be positive');
  }
  $cancel(this$static);
  this$static.isRepeating = false;
  this$static.timerId = createTimeout(this$static, delayMillis);
  $add_9(timers, this$static);
}

function clearInterval(id){
  $clinit_50();
  $wnd.clearInterval(id);
}

function clearTimeout(id){
  $clinit_50();
  $wnd.clearTimeout(id);
}

function createTimeout(timer, delay){
  $clinit_50();
  return $wnd.setTimeout(function(){
    timer.fire();
  }
  , delay);
}

function fire(){
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    $fireImpl(this);
  }
}

function hookWindowClosing(){
  $clinit_50();
  addWindowCloseListener(new Timer$1());
}

function Timer(){
}

_ = Timer.prototype = new Object_0();
_.fire = fire;
_.typeName$ = package_com_google_gwt_user_client_ + 'Timer';
_.typeId$ = 50;
_.isRepeating = false;
_.timerId = 0;
var timers;
function $clinit_36(){
  $clinit_36 = nullMethod;
  $clinit_50();
}

function $CommandExecutor$1(this$static, this$0){
  $clinit_36();
  this$static.this$0 = this$0;
  $Timer(this$static);
  return this$static;
}

function run(){
  if (!this.this$0.executing) {
    return;
  }
  $doCommandCanceled(this.this$0);
}

function CommandExecutor$1(){
}

_ = CommandExecutor$1.prototype = new Timer();
_.run = run;
_.typeName$ = package_com_google_gwt_user_client_ + 'CommandExecutor$1';
_.typeId$ = 51;
function $clinit_37(){
  $clinit_37 = nullMethod;
  $clinit_50();
}

function $CommandExecutor$2(this$static, this$0){
  $clinit_37();
  this$static.this$0 = this$0;
  $Timer(this$static);
  return this$static;
}

function run_0(){
  $setExecutionTimerPending(this.this$0, false);
  $doExecuteCommands(this.this$0, currentTimeMillis_0());
}

function CommandExecutor$2(){
}

_ = CommandExecutor$2.prototype = new Timer();
_.run = run_0;
_.typeName$ = package_com_google_gwt_user_client_ + 'CommandExecutor$2';
_.typeId$ = 52;
function $CommandExecutor$CircularIterator(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $getLast(this$static){
  return $get_0(this$static.this$0.commands, this$static.last);
}

function $hasNext(this$static){
  return this$static.next < this$static.end;
}

function $next(this$static){
  var command;
  this$static.last = this$static.next;
  command = $get_0(this$static.this$0.commands, this$static.next++);
  if (this$static.next >= this$static.end) {
    this$static.next = 0;
  }
  return command;
}

function $remove(this$static){
  $remove_12(this$static.this$0.commands, this$static.last);
  --this$static.end;
  if (this$static.last <= this$static.next) {
    if (--this$static.next < 0) {
      this$static.next = 0;
    }
  }
  this$static.last = (-1);
}

function $setEnd(this$static, end){
  this$static.end = end;
}

function $wasRemoved(this$static){
  return this$static.last == (-1);
}

function hasNext(){
  return $hasNext(this);
}

function next_0(){
  return $next(this);
}

function CommandExecutor$CircularIterator(){
}

_ = CommandExecutor$CircularIterator.prototype = new Object_0();
_.hasNext = hasNext;
_.next_0 = next_0;
_.typeName$ = package_com_google_gwt_user_client_ + 'CommandExecutor$CircularIterator';
_.typeId$ = 53;
_.end = 0;
_.last = (-1);
_.next = 0;
function $clinit_41(){
  $clinit_41 = nullMethod;
  sEventPreviewStack = $ArrayList(new ArrayList());
  {
    impl = new DOMImplSafari();
    $init(impl);
  }
}

function addEventPreview(preview){
  $clinit_41();
  $add_9(sEventPreviewStack, preview);
}

function appendChild(parent, child){
  $clinit_41();
  $appendChild(impl, parent, child);
}

function compare_0(elem1, elem2){
  $clinit_41();
  return $compare(impl, elem1, elem2);
}

function createAnchor(){
  $clinit_41();
  return $createElement(impl, 'A');
}

function createButton(){
  $clinit_41();
  return $createElement(impl, 'button');
}

function createDiv(){
  $clinit_41();
  return $createElement(impl, 'div');
}

function createElement(tagName){
  $clinit_41();
  return $createElement(impl, tagName);
}

function createImg(){
  $clinit_41();
  return $createElement(impl, 'img');
}

function createInputCheck(){
  $clinit_41();
  return $createInputElement(impl, 'checkbox');
}

function createInputPassword(){
  $clinit_41();
  return $createInputElement(impl, 'password');
}

function createInputRadio(name){
  $clinit_41();
  return $createInputRadioElement(impl, name);
}

function createInputText(){
  $clinit_41();
  return $createInputElement(impl, 'text');
}

function createLabel(){
  $clinit_41();
  return $createElement(impl, 'label');
}

function createSelect(multiple){
  $clinit_41();
  return $createSelectElement(impl, multiple);
}

function createSpan(){
  $clinit_41();
  return $createElement(impl, 'span');
}

function createTBody(){
  $clinit_41();
  return $createElement(impl, 'tbody');
}

function createTD(){
  $clinit_41();
  return $createElement(impl, 'td');
}

function createTR(){
  $clinit_41();
  return $createElement(impl, 'tr');
}

function createTable(){
  $clinit_41();
  return $createElement(impl, 'table');
}

function createTextArea(){
  $clinit_41();
  return $createElement(impl, 'textarea');
}

function dispatchEvent(evt, elem, listener){
  $clinit_41();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    dispatchEventImpl(evt, elem, listener);
  }
}

function dispatchEventImpl(evt, elem, listener){
  $clinit_41();
  var prevCurrentEvent;
  if (elem === sCaptureElem) {
    if (eventGetType(evt) == 8192) {
      sCaptureElem = null;
    }
  }
  prevCurrentEvent = currentEvent;
  currentEvent = evt;
  try {
    listener.onBrowserEvent(evt);
  }
   finally {
    currentEvent = prevCurrentEvent;
  }
}

function eventCancelBubble(evt, cancel){
  $clinit_41();
  $eventCancelBubble(impl, evt, cancel);
}

function eventGetAltKey(evt){
  $clinit_41();
  return $eventGetAltKey(impl, evt);
}

function eventGetClientX(evt){
  $clinit_41();
  return $eventGetClientX(impl, evt);
}

function eventGetClientY(evt){
  $clinit_41();
  return $eventGetClientY(impl, evt);
}

function eventGetCtrlKey(evt){
  $clinit_41();
  return $eventGetCtrlKey(impl, evt);
}

function eventGetCurrentTarget(evt){
  $clinit_41();
  return $eventGetCurrentTarget(impl, evt);
}

function eventGetFromElement(evt){
  $clinit_41();
  return $eventGetFromElement(impl, evt);
}

function eventGetKeyCode(evt){
  $clinit_41();
  return $eventGetKeyCode(impl, evt);
}

function eventGetMetaKey(evt){
  $clinit_41();
  return $eventGetMetaKey(impl, evt);
}

function eventGetShiftKey(evt){
  $clinit_41();
  return $eventGetShiftKey(impl, evt);
}

function eventGetTarget(evt){
  $clinit_41();
  return $eventGetTarget(impl, evt);
}

function eventGetToElement(evt){
  $clinit_41();
  return $eventGetToElement(impl, evt);
}

function eventGetType(evt){
  $clinit_41();
  return $eventGetTypeInt(impl, evt);
}

function eventPreventDefault(evt){
  $clinit_41();
  $eventPreventDefault(impl, evt);
}

function eventToString(evt){
  $clinit_41();
  return $eventToString(impl, evt);
}

function getAbsoluteLeft(elem){
  $clinit_41();
  return $getAbsoluteLeft(impl, elem);
}

function getAbsoluteTop(elem){
  $clinit_41();
  return $getAbsoluteTop(impl, elem);
}

function getChild(parent, index){
  $clinit_41();
  return $getChild(impl, parent, index);
}

function getChildCount(parent){
  $clinit_41();
  return $getChildCount(impl, parent);
}

function getElementProperty(elem, prop){
  $clinit_41();
  return $getElementProperty(impl, elem, prop);
}

function getElementPropertyBoolean(elem, prop){
  $clinit_41();
  return $getElementPropertyBoolean(impl, elem, prop);
}

function getElementPropertyInt(elem, prop){
  $clinit_41();
  return $getElementPropertyInt(impl, elem, prop);
}

function getEventsSunk(elem){
  $clinit_41();
  return $getEventsSunk(impl, elem);
}

function getFirstChild(elem){
  $clinit_41();
  return $getFirstChild(impl, elem);
}

function getInnerHTML(elem){
  $clinit_41();
  return $getInnerHTML(impl, elem);
}

function getParent(elem){
  $clinit_41();
  return $getParent(impl, elem);
}

function insertChild(parent, child, index){
  $clinit_41();
  $insertChild(impl, parent, child, index);
}

function insertListItem(select, item, value, index){
  $clinit_41();
  $insertListItem(impl, select, item, value, index);
}

function isOrHasChild(parent, child){
  $clinit_41();
  return $isOrHasChild(impl, parent, child);
}

function previewEvent(evt){
  $clinit_41();
  var preview, ret;
  ret = true;
  if (sEventPreviewStack.size > 0) {
    preview = dynamicCast($get_0(sEventPreviewStack, sEventPreviewStack.size - 1), 7);
    if (!(ret = preview.onEventPreview(evt))) {
      eventCancelBubble(evt, true);
      eventPreventDefault(evt);
    }
  }
  return ret;
}

function releaseCapture(elem){
  $clinit_41();
  if (sCaptureElem !== null && compare_0(elem, sCaptureElem)) {
    sCaptureElem = null;
  }
  $releaseCapture(impl, elem);
}

function removeChild(parent, child){
  $clinit_41();
  $removeChild(impl, parent, child);
}

function removeElementAttribute(elem, attr){
  $clinit_41();
  $removeElementAttribute(impl, elem, attr);
}

function removeEventPreview(preview){
  $clinit_41();
  $remove_13(sEventPreviewStack, preview);
}

function scrollIntoView(elem){
  $clinit_41();
  $scrollIntoView(impl, elem);
}

function setCapture(elem){
  $clinit_41();
  sCaptureElem = elem;
  $setCapture(impl, elem);
}

function setElementAttribute(elem, attr, value){
  $clinit_41();
  $setElementAttribute(impl, elem, attr, value);
}

function setElementProperty(elem, prop, value){
  $clinit_41();
  $setElementProperty(impl, elem, prop, value);
}

function setElementPropertyBoolean(elem, prop, value){
  $clinit_41();
  $setElementPropertyBoolean(impl, elem, prop, value);
}

function setElementPropertyInt(elem, prop, value){
  $clinit_41();
  $setElementPropertyInt(impl, elem, prop, value);
}

function setEventListener(elem, listener){
  $clinit_41();
  $setEventListener(impl, elem, listener);
}

function setImgSrc(img, src){
  $clinit_41();
  $setImgSrc(impl, img, src);
}

function setInnerHTML(elem, html){
  $clinit_41();
  $setInnerHTML(impl, elem, html);
}

function setInnerText(elem, text){
  $clinit_41();
  $setInnerText(impl, elem, text);
}

function setIntStyleAttribute(elem, attr, value){
  $clinit_41();
  $setIntStyleAttribute(impl, elem, attr, value);
}

function setStyleAttribute(elem, attr, value){
  $clinit_41();
  $setStyleAttribute(impl, elem, attr, value);
}

function sinkEvents(elem, eventBits){
  $clinit_41();
  $sinkEvents(impl, elem, eventBits);
}

function toString_1(elem){
  $clinit_41();
  return $toString(impl, elem);
}

function windowGetClientHeight(){
  $clinit_41();
  return $windowGetClientHeight(impl);
}

function windowGetClientWidth(){
  $clinit_41();
  return $windowGetClientWidth(impl);
}

var currentEvent = null, impl = null, sCaptureElem = null, sEventPreviewStack;
function $clinit_42(){
  $clinit_42 = nullMethod;
  commandExecutor = $CommandExecutor(new CommandExecutor());
}

function addCommand(cmd){
  $clinit_42();
  if (cmd === null) {
    throw $NullPointerException(new NullPointerException(), 'cmd can not be null');
  }
  $submit(commandExecutor, cmd);
}

var commandExecutor;
function $equals_0(this$static, other){
  if (instanceOf(other, 8)) {
    return compare_0(this$static, dynamicCast(other, 8));
  }
  return $equals(wrapJSO(this$static, Element), other);
}

function equals_0(other){
  return $equals_0(this, other);
}

function hashCode_1(){
  return $hashCode(wrapJSO(this, Element));
}

function toString_2(){
  return toString_1(this);
}

function Element(){
}

_ = Element.prototype = new JavaScriptObject();
_.equals$ = equals_0;
_.hashCode$ = hashCode_1;
_.toString$ = toString_2;
_.typeName$ = package_com_google_gwt_user_client_ + 'Element';
_.typeId$ = 54;
function equals_1(other){
  return $equals(wrapJSO(this, Event), other);
}

function hashCode_2(){
  return $hashCode(wrapJSO(this, Event));
}

function toString_3(){
  return eventToString(this);
}

function Event(){
}

_ = Event.prototype = new JavaScriptObject();
_.equals$ = equals_1;
_.hashCode$ = hashCode_2;
_.toString$ = toString_3;
_.typeName$ = package_com_google_gwt_user_client_ + 'Event';
_.typeId$ = 55;
function $clinit_48(){
  $clinit_48 = nullMethod;
  historyListeners = $ArrayList(new ArrayList());
  {
    impl_0 = $HistoryImplSafari(new HistoryImplSafari());
    if (!$init_0(impl_0)) {
      impl_0 = null;
    }
  }
}

function addHistoryListener(listener){
  $clinit_48();
  $add_9(historyListeners, listener);
}

function fireHistoryChangedImpl(historyToken){
  $clinit_48();
  var it, listener;
  for (it = $iterator_3(historyListeners); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 9);
    listener.onHistoryChanged(historyToken);
  }
}

function getToken(){
  $clinit_48();
  return impl_0 !== null?$getToken(impl_0):'';
}

function newItem(historyToken){
  $clinit_48();
  if (impl_0 !== null) {
    $newItem(impl_0, historyToken);
  }
}

function onHistoryChanged_0(historyToken){
  $clinit_48();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    fireHistoryChangedImpl(historyToken);
  }
}

var historyListeners, impl_0 = null;
function onWindowClosed(){
  while (($clinit_50() , timers).size > 0) {
    $cancel(dynamicCast($get_0(($clinit_50() , timers), 0), 10));
  }
}

function onWindowClosing(){
  return null;
}

function Timer$1(){
}

_ = Timer$1.prototype = new Object_0();
_.onWindowClosed = onWindowClosed;
_.onWindowClosing = onWindowClosing;
_.typeName$ = package_com_google_gwt_user_client_ + 'Timer$1';
_.typeId$ = 56;
function $clinit_52(){
  $clinit_52 = nullMethod;
  closingListeners = $ArrayList(new ArrayList());
  resizeListeners = $ArrayList(new ArrayList());
  {
    init_5();
  }
}

function addWindowCloseListener(listener){
  $clinit_52();
  $add_9(closingListeners, listener);
}

function alert(msg){
  $clinit_52();
  $wnd.alert(msg);
}

function fireClosedImpl(){
  $clinit_52();
  var it, listener;
  for (it = $iterator_3(closingListeners); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 11);
    listener.onWindowClosed();
  }
}

function fireClosingImpl(){
  $clinit_52();
  var it, listener, msg, ret;
  ret = null;
  for (it = $iterator_3(closingListeners); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 11);
    msg = listener.onWindowClosing();
    {
      ret = msg;
    }
  }
  return ret;
}

function fireResizedImpl(){
  $clinit_52();
  var it, listener;
  for (it = $iterator_3(resizeListeners); $hasNext_4(it);) {
    listener = throwClassCastExceptionUnlessNull($next_3(it));
    null.nullMethod();
  }
}

function getClientHeight(){
  $clinit_52();
  return windowGetClientHeight();
}

function getClientWidth(){
  $clinit_52();
  return windowGetClientWidth();
}

function getScrollLeft(){
  $clinit_52();
  return $doc.documentElement.scrollLeft || $doc.body.scrollLeft;
}

function getScrollTop(){
  $clinit_52();
  return $doc.documentElement.scrollTop || $doc.body.scrollTop;
}

function init_5(){
  $clinit_52();
  __gwt_initHandlers(function(){
    onResize();
  }
  , function(){
    return onClosing();
  }
  , function(){
    onClosed();
    $wnd.onresize = null;
    $wnd.onbeforeclose = null;
    $wnd.onclose = null;
  }
  );
}

function onClosed(){
  $clinit_52();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    fireClosedImpl();
  }
}

function onClosing(){
  $clinit_52();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    return fireClosingImpl();
  }
}

function onResize(){
  $clinit_52();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    fireResizedImpl();
  }
}

function prompt(msg, initialValue){
  $clinit_52();
  return $wnd.prompt(msg, initialValue);
}

var closingListeners, resizeListeners;
function $appendChild(this$static, parent, child){
  parent.appendChild(child);
}

function $createElement(this$static, tag){
  return $doc.createElement(tag);
}

function $createInputElement(this$static, type){
  var e = $doc.createElement('INPUT');
  e.type = type;
  return e;
}

function $createSelectElement(this$static, multiple){
  var select;
  select = $createElement(this$static, 'select');
  if (multiple) {
    $setElementPropertyBoolean(this$static, select, 'multiple', true);
  }
  return select;
}

function $eventCancelBubble(this$static, evt, cancel){
  evt.cancelBubble = cancel;
}

function $eventGetAltKey(this$static, evt){
  return !(!evt.altKey);
}

function $eventGetCtrlKey(this$static, evt){
  return !(!evt.ctrlKey);
}

function $eventGetCurrentTarget(this$static, evt){
  return evt.currentTarget;
}

function $eventGetKeyCode(this$static, evt){
  return evt.which || (evt.keyCode || -1);
}

function $eventGetMetaKey(this$static, evt){
  return !(!evt.metaKey);
}

function $eventGetShiftKey(this$static, evt){
  return !(!evt.shiftKey);
}

function $eventGetTypeInt(this$static, evt){
  switch (evt.type) {
    case 'blur':
      return 4096;
    case 'change':
      return 1024;
    case 'click':
      return 1;
    case 'dblclick':
      return 2;
    case 'focus':
      return 2048;
    case 'keydown':
      return 128;
    case 'keypress':
      return 256;
    case 'keyup':
      return 512;
    case 'load':
      return 32768;
    case 'losecapture':
      return 8192;
    case 'mousedown':
      return 4;
    case 'mousemove':
      return 64;
    case 'mouseout':
      return 32;
    case 'mouseover':
      return 16;
    case 'mouseup':
      return 8;
    case 'scroll':
      return 16384;
    case 'error':
      return 65536;
    case 'mousewheel':
      return 131072;
    case 'DOMMouseScroll':
      return 131072;
  }
}

function $getElementProperty(this$static, elem, prop){
  var ret = elem[prop];
  return ret == null?null:String(ret);
}

function $getElementPropertyBoolean(this$static, elem, prop){
  return !(!elem[prop]);
}

function $getElementPropertyInt(this$static, elem, prop){
  var i = parseInt(elem[prop]);
  if (!i) {
    return 0;
  }
  return i;
}

function $getEventsSunk(this$static, elem){
  return elem.__eventBits || 0;
}

function $getInnerHTML(this$static, elem){
  var ret = elem.innerHTML;
  return ret == null?null:ret;
}

function $removeChild(this$static, parent, child){
  parent.removeChild(child);
}

function $removeElementAttribute(this$static, elem, attr){
  elem.removeAttribute(attr);
}

function $scrollIntoView(this$static, elem){
  var left = elem.offsetLeft, top = elem.offsetTop;
  var width = elem.offsetWidth, height = elem.offsetHeight;
  if (elem.parentNode != elem.offsetParent) {
    left -= elem.parentNode.offsetLeft;
    top -= elem.parentNode.offsetTop;
  }
  var cur = elem.parentNode;
  while (cur && cur.nodeType == 1) {
    if (cur.style.overflow == 'auto' || (cur.style.overflow == 'scroll' || cur.tagName == 'BODY')) {
      if (left < cur.scrollLeft) {
        cur.scrollLeft = left;
      }
      if (left + width > cur.scrollLeft + cur.clientWidth) {
        cur.scrollLeft = left + width - cur.clientWidth;
      }
      if (top < cur.scrollTop) {
        cur.scrollTop = top;
      }
      if (top + height > cur.scrollTop + cur.clientHeight) {
        cur.scrollTop = top + height - cur.clientHeight;
      }
    }
    var offsetLeft = cur.offsetLeft, offsetTop = cur.offsetTop;
    if (cur.parentNode != cur.offsetParent) {
      offsetLeft -= cur.parentNode.offsetLeft;
      offsetTop -= cur.parentNode.offsetTop;
    }
    left += offsetLeft - cur.scrollLeft;
    top += offsetTop - cur.scrollTop;
    cur = cur.parentNode;
  }
}

function $setElementAttribute(this$static, elem, attr, value){
  elem.setAttribute(attr, value);
}

function $setElementProperty(this$static, elem, prop, value){
  elem[prop] = value;
}

function $setElementPropertyBoolean(this$static, elem, prop, value){
  elem[prop] = value;
}

function $setElementPropertyInt(this$static, elem, prop, value){
  elem[prop] = value;
}

function $setEventListener(this$static, elem, listener){
  elem.__listener = listener;
}

function $setImgSrc(this$static, img, src){
  img.src = src;
}

function $setInnerHTML(this$static, elem, html){
  if (!html) {
    html = '';
  }
  elem.innerHTML = html;
}

function $setInnerText(this$static, elem, text){
  while (elem.firstChild) {
    elem.removeChild(elem.firstChild);
  }
  if (text != null) {
    elem.appendChild($doc.createTextNode(text));
  }
}

function $setIntStyleAttribute(this$static, elem, attr, value){
  elem.style[attr] = value;
}

function $setStyleAttribute(this$static, elem, attr, value){
  elem.style[attr] = value;
}

function $toString(this$static, elem){
  return elem.outerHTML;
}

function DOMImpl(){
}

_ = DOMImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_impl_ + 'DOMImpl';
_.typeId$ = 57;
function $compare(this$static, elem1, elem2){
  return elem1 == elem2;
}

function $createInputRadioElement(this$static, name){
  var elem = $doc.createElement('INPUT');
  elem.type = 'radio';
  elem.name = name;
  return elem;
}

function $eventGetFromElement(this$static, evt){
  return evt.relatedTarget?evt.relatedTarget:null;
}

function $eventGetTarget(this$static, evt){
  return evt.target || null;
}

function $eventGetToElement(this$static, evt){
  return evt.relatedTarget || null;
}

function $eventPreventDefault(this$static, evt){
  evt.preventDefault();
}

function $eventToString(this$static, evt){
  return evt.toString();
}

function $getChild(this$static, elem, index){
  var count = 0, child = elem.firstChild;
  while (child) {
    var next = child.nextSibling;
    if (child.nodeType == 1) {
      if (index == count)
        return child;
      ++count;
    }
    child = next;
  }
  return null;
}

function $getChildCount(this$static, elem){
  var count = 0, child = elem.firstChild;
  while (child) {
    if (child.nodeType == 1)
      ++count;
    child = child.nextSibling;
  }
  return count;
}

function $getFirstChild(this$static, elem){
  var child = elem.firstChild;
  while (child && child.nodeType != 1)
    child = child.nextSibling;
  return child || null;
}

function $getParent(this$static, elem){
  var parent = elem.parentNode;
  if (parent == null) {
    return null;
  }
  if (parent.nodeType != 1)
    parent = null;
  return parent || null;
}

function $init(this$static){
  $wnd.__dispatchCapturedMouseEvent = function(evt){
    if ($wnd.__dispatchCapturedEvent(evt)) {
      var cap = $wnd.__captureElem;
      if (cap && cap.__listener) {
        dispatchEvent(evt, cap, cap.__listener);
        evt.stopPropagation();
      }
    }
  }
  ;
  $wnd.__dispatchCapturedEvent = function(evt){
    if (!previewEvent(evt)) {
      evt.stopPropagation();
      evt.preventDefault();
      return false;
    }
    return true;
  }
  ;
  $wnd.addEventListener('click', $wnd.__dispatchCapturedMouseEvent, true);
  $wnd.addEventListener('dblclick', $wnd.__dispatchCapturedMouseEvent, true);
  $wnd.addEventListener('mousedown', $wnd.__dispatchCapturedMouseEvent, true);
  $wnd.addEventListener('mouseup', $wnd.__dispatchCapturedMouseEvent, true);
  $wnd.addEventListener('mousemove', $wnd.__dispatchCapturedMouseEvent, true);
  $wnd.addEventListener('mousewheel', $wnd.__dispatchCapturedMouseEvent, true);
  $wnd.addEventListener('keydown', $wnd.__dispatchCapturedEvent, true);
  $wnd.addEventListener('keyup', $wnd.__dispatchCapturedEvent, true);
  $wnd.addEventListener('keypress', $wnd.__dispatchCapturedEvent, true);
  $wnd.__dispatchEvent = function(evt){
    var listener, curElem = this;
    while (curElem && !(listener = curElem.__listener))
      curElem = curElem.parentNode;
    if (curElem && curElem.nodeType != 1)
      curElem = null;
    if (listener)
      dispatchEvent(evt, curElem, listener);
  }
  ;
  $wnd.__captureElem = null;
}

function $insertChild(this$static, parent, toAdd, index){
  var count = 0, child = parent.firstChild, before = null;
  while (child) {
    if (child.nodeType == 1) {
      if (count == index) {
        before = child;
        break;
      }
      ++count;
    }
    child = child.nextSibling;
  }
  parent.insertBefore(toAdd, before);
}

function $isOrHasChild(this$static, parent, child){
  while (child) {
    if (parent == child) {
      return true;
    }
    child = child.parentNode;
    if (child && child.nodeType != 1) {
      child = null;
    }
  }
  return false;
}

function $releaseCapture(this$static, elem){
  if (elem == $wnd.__captureElem)
    $wnd.__captureElem = null;
}

function $setCapture(this$static, elem){
  $wnd.__captureElem = elem;
}

function $sinkEvents(this$static, elem, bits){
  elem.__eventBits = bits;
  elem.onclick = bits & 1?$wnd.__dispatchEvent:null;
  elem.ondblclick = bits & 2?$wnd.__dispatchEvent:null;
  elem.onmousedown = bits & 4?$wnd.__dispatchEvent:null;
  elem.onmouseup = bits & 8?$wnd.__dispatchEvent:null;
  elem.onmouseover = bits & 16?$wnd.__dispatchEvent:null;
  elem.onmouseout = bits & 32?$wnd.__dispatchEvent:null;
  elem.onmousemove = bits & 64?$wnd.__dispatchEvent:null;
  elem.onkeydown = bits & 128?$wnd.__dispatchEvent:null;
  elem.onkeypress = bits & 256?$wnd.__dispatchEvent:null;
  elem.onkeyup = bits & 512?$wnd.__dispatchEvent:null;
  elem.onchange = bits & 1024?$wnd.__dispatchEvent:null;
  elem.onfocus = bits & 2048?$wnd.__dispatchEvent:null;
  elem.onblur = bits & 4096?$wnd.__dispatchEvent:null;
  elem.onlosecapture = bits & 8192?$wnd.__dispatchEvent:null;
  elem.onscroll = bits & 16384?$wnd.__dispatchEvent:null;
  elem.onload = bits & 32768?$wnd.__dispatchEvent:null;
  elem.onerror = bits & 65536?$wnd.__dispatchEvent:null;
  elem.onmousewheel = bits & 131072?$wnd.__dispatchEvent:null;
}

function DOMImplStandard(){
}

_ = DOMImplStandard.prototype = new DOMImpl();
_.typeName$ = package_com_google_gwt_user_client_impl_ + 'DOMImplStandard';
_.typeId$ = 58;
function $eventGetClientX(this$static, evt){
  return evt.pageX - $doc.body.scrollLeft || -1;
}

function $eventGetClientY(this$static, evt){
  return evt.pageY - $doc.body.scrollTop || -1;
}

function $getAbsoluteLeft(this$static, elem){
  if (elem.offsetLeft == null) {
    return 0;
  }
  var left = 0;
  var curr = elem.parentNode;
  if (curr) {
    while (curr.offsetParent) {
      left -= curr.scrollLeft;
      curr = curr.parentNode;
    }
  }
  while (elem) {
    left += elem.offsetLeft;
    var parent = elem.offsetParent;
    if (parent && (parent.tagName == 'BODY' && elem.style.position == 'absolute')) {
      break;
    }
    elem = parent;
  }
  return left;
}

function $getAbsoluteTop(this$static, elem){
  if (elem.offsetTop == null) {
    return 0;
  }
  var top = 0;
  var curr = elem.parentNode;
  if (curr) {
    while (curr.offsetParent) {
      top -= curr.scrollTop;
      curr = curr.parentNode;
    }
  }
  while (elem) {
    top += elem.offsetTop;
    var parent = elem.offsetParent;
    if (parent && (parent.tagName == 'BODY' && elem.style.position == 'absolute')) {
      break;
    }
    elem = parent;
  }
  return top;
}

function $insertListItem(this$static, select, text, value, index){
  var newOption = new Option(text, value);
  if (index == -1 || index > select.children.length - 1) {
    select.appendChild(newOption);
  }
   else {
    select.insertBefore(newOption, select.children[index]);
  }
}

function $windowGetClientHeight(this$static){
  return $wnd.innerHeight;
}

function $windowGetClientWidth(this$static){
  return $wnd.innerWidth;
}

function DOMImplSafari(){
}

_ = DOMImplSafari.prototype = new DOMImplStandard();
_.typeName$ = package_com_google_gwt_user_client_impl_ + 'DOMImplSafari';
_.typeId$ = 59;
function $getToken(this$static){
  return $wnd.__gwt_historyToken;
}

function onHistoryChanged_1(historyToken){
  onHistoryChanged_0(historyToken);
}

function HistoryImpl(){
}

_ = HistoryImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_impl_ + 'HistoryImpl';
_.typeId$ = 60;
function $init_1(this$static){
  $wnd.__gwt_historyToken = '';
  var hash_0 = $wnd.location.hash;
  if (hash_0.length > 0)
    $wnd.__gwt_historyToken = hash_0.substring(1);
  $wnd.__checkHistory = function(){
    var token = '', hash = $wnd.location.hash;
    if (hash.length > 0)
      token = hash.substring(1);
    if (token != $wnd.__gwt_historyToken) {
      $wnd.__gwt_historyToken = token;
      onHistoryChanged_1(token);
    }
    $wnd.setTimeout('__checkHistory()', 250);
  }
  ;
  $wnd.__checkHistory();
  return true;
}

function $newItem_0(this$static, historyToken){
  if (historyToken == null) {
    historyToken = '';
  }
  $wnd.location.hash = encodeURIComponent(historyToken);
}

function HistoryImplStandard(){
}

_ = HistoryImplStandard.prototype = new HistoryImpl();
_.typeName$ = package_com_google_gwt_user_client_impl_ + 'HistoryImplStandard';
_.typeId$ = 61;
function $clinit_56(){
  $clinit_56 = nullMethod;
  isOldSafari = detectOldSafari();
}

function $HistoryImplSafari(this$static){
  $clinit_56();
  return this$static;
}

function $init_0(this$static){
  if (isOldSafari) {
    $initImpl(this$static);
    return true;
  }
  return $init_1(this$static);
}

function $initImpl(this$static){
  $wnd.__gwt_historyToken = '';
  var hash = $wnd.location.hash;
  if (hash.length > 0)
    $wnd.__gwt_historyToken = decodeURIComponent(hash.substring(1));
  onHistoryChanged_1($wnd.__gwt_historyToken);
}

function $newItem(this$static, historyToken){
  if (isOldSafari) {
    $newItemImpl(this$static, historyToken);
    return;
  }
  $newItem_0(this$static, historyToken);
}

function $newItemImpl(this$static, historyToken){
  var meta = $doc.createElement('meta');
  meta.setAttribute('http-equiv', 'refresh');
  var newUrl = $wnd.location.href.split('#')[0] + '#' + encodeURIComponent(historyToken);
  meta.setAttribute('content', '0.01;url=' + newUrl);
  $doc.body.appendChild(meta);
  window.setTimeout(function(){
    $doc.body.removeChild(meta);
  }
  , 1);
  $wnd.__gwt_historyToken = historyToken;
  onHistoryChanged_1($wnd.__gwt_historyToken);
}

function detectOldSafari(){
  $clinit_56();
  var exp = / AppleWebKit\/([\d]+)/;
  var result = exp.exec(navigator.userAgent);
  if (result) {
    if (parseInt(result[1]) >= 522) {
      return false;
    }
  }
  if (navigator.userAgent.indexOf('iPhone') != -1) {
    return false;
  }
  return true;
}

function HistoryImplSafari(){
}

_ = HistoryImplSafari.prototype = new HistoryImplStandard();
_.typeName$ = package_com_google_gwt_user_client_impl_ + 'HistoryImplSafari';
_.typeId$ = 62;
var isOldSafari;
function $$init_7(this$static){
  this$static.children_0 = $WidgetCollection(new WidgetCollection(), this$static);
}

function $ComplexPanel(this$static){
  $$init_7(this$static);
  return this$static;
}

function $add_0(this$static, child, container){
  $removeFromParent(child);
  $add_8(this$static.children_0, child);
  appendChild(container, child.getElement());
  $adopt(this$static, child);
}

function $adjustIndex(this$static, child, beforeIndex){
  var idx;
  $checkIndexBoundsForInsertion(this$static, beforeIndex);
  if (child.parent === this$static) {
    idx = $getWidgetIndex(this$static, child);
    if (idx < beforeIndex) {
      beforeIndex--;
    }
  }
  return beforeIndex;
}

function $checkIndexBoundsForAccess(this$static, index){
  if (index < 0 || index >= this$static.children_0.size) {
    throw new IndexOutOfBoundsException();
  }
}

function $checkIndexBoundsForInsertion(this$static, index){
  if (index < 0 || index > this$static.children_0.size) {
    throw new IndexOutOfBoundsException();
  }
}

function $getWidget(this$static, index){
  return $get(this$static.children_0, index);
}

function $getWidgetIndex(this$static, child){
  return $indexOf(this$static.children_0, child);
}

function $insert(this$static, child, container, beforeIndex, domInsert){
  beforeIndex = $adjustIndex(this$static, child, beforeIndex);
  $removeFromParent(child);
  $insert_4(this$static.children_0, child, beforeIndex);
  if (domInsert) {
    insertChild(container, child.getElement(), beforeIndex);
  }
   else {
    appendChild(container, child.getElement());
  }
  $adopt(this$static, child);
}

function $iterator(this$static){
  return $iterator_2(this$static.children_0);
}

function $remove_1(this$static, w){
  var elem;
  if (w.parent !== this$static) {
    return false;
  }
  $orphan(this$static, w);
  elem = w.getElement();
  removeChild(getParent(elem), elem);
  $remove_9(this$static.children_0, w);
  return true;
}

function iterator(){
  return $iterator(this);
}

function remove_1(w){
  return $remove_1(this, w);
}

function ComplexPanel(){
}

_ = ComplexPanel.prototype = new Panel();
_.iterator_0 = iterator;
_.remove_0 = remove_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ComplexPanel';
_.typeId$ = 63;
function $AbsolutePanel(this$static){
  $ComplexPanel(this$static);
  this$static.setElement(createDiv());
  setStyleAttribute(this$static.getElement(), 'position', 'relative');
  setStyleAttribute(this$static.getElement(), 'overflow', 'hidden');
  return this$static;
}

function $add(this$static, w){
  $add_0(this$static, w, this$static.getElement());
}

function $remove_0(this$static, w){
  var removed;
  removed = $remove_1(this$static, w);
  if (removed) {
    changeToStaticPositioning(w.getElement());
  }
  return removed;
}

function changeToStaticPositioning(elem){
  setStyleAttribute(elem, 'left', '');
  setStyleAttribute(elem, 'top', '');
  setStyleAttribute(elem, 'position', '');
}

function remove_0(w){
  return $remove_0(this, w);
}

function AbsolutePanel(){
}

_ = AbsolutePanel.prototype = new ComplexPanel();
_.remove_0 = remove_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'AbsolutePanel';
_.typeId$ = 64;
function AbstractImagePrototype(){
}

_ = AbstractImagePrototype.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'AbstractImagePrototype';
_.typeId$ = 65;
function $clinit_94(){
  $clinit_94 = nullMethod;
  $clinit_217() , implWidget;
}

function $FocusWidget(this$static){
  $clinit_217() , implWidget;
  return this$static;
}

function $FocusWidget_0(this$static, elem){
  $clinit_217() , implWidget;
  $setElement(this$static, elem);
  return this$static;
}

function $fireClickListeners(this$static){
  if (this$static.clickListeners_0 !== null) {
    $fireClick(this$static.clickListeners_0, this$static);
  }
}

function $onBrowserEvent(this$static, event_0){
  switch (eventGetType(event_0)) {
    case 1:
      if (this$static.clickListeners_0 !== null) {
        $fireClick(this$static.clickListeners_0, this$static);
      }

      break;
    case 4096:
    case 2048:
      break;
    case 128:
    case 512:
    case 256:
      if (this$static.keyboardListeners_0 !== null) {
        $fireKeyboardEvent(this$static.keyboardListeners_0, this$static, event_0);
      }

      break;
  }
}

function $setElement(this$static, elem){
  $setElement_1(this$static, elem);
  $sinkEvents_0(this$static, 7041);
}

function $setEnabled_0(this$static, enabled){
  setElementPropertyBoolean(this$static.getElement(), 'disabled', !enabled);
}

function addClickListener(listener){
  if (this.clickListeners_0 === null) {
    this.clickListeners_0 = $ClickListenerCollection(new ClickListenerCollection());
  }
  $add_9(this.clickListeners_0, listener);
}

function addKeyboardListener(listener){
  if (this.keyboardListeners_0 === null) {
    this.keyboardListeners_0 = $KeyboardListenerCollection(new KeyboardListenerCollection());
  }
  $add_9(this.keyboardListeners_0, listener);
}

function isEnabled_0(){
  return !getElementPropertyBoolean(this.getElement(), 'disabled');
}

function onBrowserEvent_2(event_0){
  $onBrowserEvent(this, event_0);
}

function setElement(elem){
  $setElement(this, elem);
}

function setEnabled_1(enabled){
  $setEnabled_0(this, enabled);
}

function FocusWidget(){
}

_ = FocusWidget.prototype = new Widget();
_.addClickListener = addClickListener;
_.addKeyboardListener = addKeyboardListener;
_.isEnabled = isEnabled_0;
_.onBrowserEvent = onBrowserEvent_2;
_.setElement = setElement;
_.setEnabled = setEnabled_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'FocusWidget';
_.typeId$ = 66;
_.clickListeners_0 = null;
_.keyboardListeners_0 = null;
function $clinit_62(){
  $clinit_62 = nullMethod;
  $clinit_217() , implWidget;
}

function $ButtonBase(this$static, elem){
  $clinit_217() , implWidget;
  $FocusWidget_0(this$static, elem);
  return this$static;
}

function setHTML(html){
  setInnerHTML(this.getElement(), html);
}

function ButtonBase(){
}

_ = ButtonBase.prototype = new FocusWidget();
_.setHTML = setHTML;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ButtonBase';
_.typeId$ = 67;
function $clinit_63(){
  $clinit_63 = nullMethod;
  $clinit_217() , implWidget;
}

function $Button(this$static){
  $clinit_217() , implWidget;
  $ButtonBase(this$static, createButton());
  adjustType(this$static.getElement());
  $setStyleName(this$static, 'gwt-Button');
  return this$static;
}

function $Button_0(this$static, html){
  $clinit_217() , implWidget;
  $Button(this$static);
  this$static.setHTML(html);
  return this$static;
}

function $Button_1(this$static, html, listener){
  $clinit_217() , implWidget;
  $Button_0(this$static, html);
  this$static.addClickListener(listener);
  return this$static;
}

function adjustType(button){
  $clinit_63();
  if (button.type == 'submit') {
    try {
      button.setAttribute('type', 'button');
    }
     catch (e) {
    }
  }
}

function Button(){
}

_ = Button.prototype = new ButtonBase();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Button';
_.typeId$ = 68;
function $CellPanel(this$static){
  $ComplexPanel(this$static);
  this$static.table = createTable();
  this$static.body_0 = createTBody();
  appendChild(this$static.table, this$static.body_0);
  this$static.setElement(this$static.table);
  return this$static;
}

function $getWidgetTd(this$static, w){
  if (w.parent !== this$static) {
    return null;
  }
  return getParent(w.getElement());
}

function $setCellHorizontalAlignment(this$static, td, align){
  setElementProperty(td, 'align', align.textAlignString);
}

function $setCellVerticalAlignment(this$static, td, align){
  setStyleAttribute(td, 'verticalAlign', align.verticalAlignString);
}

function $setSpacing(this$static, spacing){
  setElementPropertyInt(this$static.table, 'cellSpacing', spacing);
}

function setCellHeight(w, height){
  var td;
  td = getParent(w.getElement());
  setElementProperty(td, 'height', height);
}

function setCellHorizontalAlignment(w, align){
  var td;
  td = $getWidgetTd(this, w);
  if (td !== null) {
    $setCellHorizontalAlignment(this, td, align);
  }
}

function setCellVerticalAlignment(w, align){
  var td;
  td = $getWidgetTd(this, w);
  if (td !== null) {
    $setCellVerticalAlignment(this, td, align);
  }
}

function setCellWidth(w, width){
  var td;
  td = getParent(w.getElement());
  setElementProperty(td, 'width', width);
}

function CellPanel(){
}

_ = CellPanel.prototype = new ComplexPanel();
_.setCellHeight = setCellHeight;
_.setCellHorizontalAlignment = setCellHorizontalAlignment;
_.setCellVerticalAlignment = setCellVerticalAlignment;
_.setCellWidth = setCellWidth;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'CellPanel';
_.typeId$ = 69;
_.body_0 = null;
_.table = null;
function $advanceToFind(this$static, iter, o){
  var t;
  while (iter.hasNext()) {
    t = iter.next_0();
    if (o === null?t === null:o.equals$(t)) {
      return iter;
    }
  }
  return null;
}

function $retainAll(this$static, c){
  var changed, iter;
  iter = $iterator_7(this$static);
  changed = false;
  while ($hasNext_5(iter)) {
    if (!$contains_1(c, $next_4(iter))) {
      $remove_11(iter);
      changed = true;
    }
  }
  return changed;
}

function add_2(o){
  throw $UnsupportedOperationException(new UnsupportedOperationException(), 'add');
}

function addAll(c){
  var changed, iter;
  iter = c.iterator_0();
  changed = false;
  while (iter.hasNext()) {
    if (this.add_0(iter.next_0())) {
      changed = true;
    }
  }
  return changed;
}

function contains_0(o){
  var iter;
  iter = $advanceToFind(this, this.iterator_0(), o);
  return iter !== null;
}

function toArray(){
  return this.toArray_0(initDims_0('[Ljava.lang.Object;', [201], [23], [this.size_0()], null));
}

function toArray_0(a){
  var i, it, size;
  size = this.size_0();
  if (a.length_0 < size) {
    a = clonify_0(a, size);
  }
  i = 0;
  for (it = this.iterator_0(); it.hasNext();) {
    setCheck(a, i++, it.next_0());
  }
  if (a.length_0 > size) {
    setCheck(a, size, null);
  }
  return a;
}

function toString_14(){
  var comma, iter, sb;
  sb = $StringBuffer(new StringBuffer());
  comma = null;
  $append(sb, '[');
  iter = this.iterator_0();
  while (iter.hasNext()) {
    if (comma !== null) {
      $append(sb, comma);
    }
     else {
      comma = ', ';
    }
    $append(sb, valueOf_1(iter.next_0()));
  }
  $append(sb, ']');
  return $toString_0(sb);
}

function AbstractCollection(){
}

_ = AbstractCollection.prototype = new Object_0();
_.add_0 = add_2;
_.addAll = addAll;
_.contains = contains_0;
_.toArray = toArray;
_.toArray_0 = toArray_0;
_.toString$ = toString_14;
_.typeName$ = package_java_util_ + 'AbstractCollection';
_.typeId$ = 70;
function $indexOutOfBounds(this$static, i){
  throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Index: ' + i + ', Size: ' + this$static.size);
}

function $iterator_3(this$static){
  return $AbstractList$IteratorImpl(new AbstractList$IteratorImpl(), this$static);
}

function add_3(index, element){
  throw $UnsupportedOperationException(new UnsupportedOperationException(), 'add');
}

function add_4(obj){
  this.add(this.size_0(), obj);
  return true;
}

function equals_5(o){
  var elem, elemOther, iter, iterOther, other;
  if (o === this) {
    return true;
  }
  if (!instanceOf(o, 42)) {
    return false;
  }
  other = dynamicCast(o, 42);
  if (this.size_0() != other.size_0()) {
    return false;
  }
  iter = $iterator_3(this);
  iterOther = other.iterator_0();
  while ($hasNext_4(iter)) {
    elem = $next_3(iter);
    elemOther = $next_3(iterOther);
    if (!(elem === null?elemOther === null:elem.equals$(elemOther))) {
      return false;
    }
  }
  return true;
}

function hashCode_6(){
  var coeff, iter, k, obj;
  k = 1;
  coeff = 31;
  iter = $iterator_3(this);
  while ($hasNext_4(iter)) {
    obj = $next_3(iter);
    k = 31 * k + (obj === null?0:obj.hashCode$());
  }
  return k;
}

function iterator_7(){
  return $iterator_3(this);
}

function remove_14(index){
  throw $UnsupportedOperationException(new UnsupportedOperationException(), 'remove');
}

function AbstractList(){
}

_ = AbstractList.prototype = new AbstractCollection();
_.add = add_3;
_.add_0 = add_4;
_.equals$ = equals_5;
_.hashCode$ = hashCode_6;
_.iterator_0 = iterator_7;
_.remove = remove_14;
_.typeName$ = package_java_util_ + 'AbstractList';
_.typeId$ = 71;
function $$init_33(this$static){
  {
    $clearImpl(this$static);
  }
}

function $ArrayList(this$static){
  $$init_33(this$static);
  return this$static;
}

function $add_9(this$static, o){
  setImpl(this$static.array, this$static.size++, o);
  return true;
}

function $addAll(this$static, c){
  var changed, iter;
  iter = c.iterator_0();
  changed = iter.hasNext();
  while (iter.hasNext()) {
    setImpl(this$static.array, this$static.size++, iter.next_0());
  }
  return changed;
}

function $clear_2(this$static){
  $clearImpl(this$static);
}

function $clearImpl(this$static){
  this$static.array = createArray();
  this$static.size = 0;
}

function $contains_0(this$static, o){
  return $indexOf_3(this$static, o) != (-1);
}

function $get_0(this$static, index){
  if (index < 0 || index >= this$static.size) {
    $indexOutOfBounds(this$static, index);
  }
  return getImpl(this$static.array, index);
}

function $indexOf_3(this$static, o){
  return $indexOf_4(this$static, o, 0);
}

function $indexOf_4(this$static, o, index){
  if (index < 0) {
    $indexOutOfBounds(this$static, index);
  }
  for (; index < this$static.size; ++index) {
    if (equals_8(o, getImpl(this$static.array, index))) {
      return index;
    }
  }
  return (-1);
}

function $isEmpty(this$static){
  return this$static.size == 0;
}

function $remove_12(this$static, index){
  var previous;
  previous = $get_0(this$static, index);
  removeRangeImpl(this$static.array, index, 1);
  --this$static.size;
  return previous;
}

function $remove_13(this$static, o){
  var i;
  i = $indexOf_3(this$static, o);
  if (i == (-1)) {
    return false;
  }
  $remove_12(this$static, i);
  return true;
}

function $set(this$static, index, o){
  var previous;
  previous = $get_0(this$static, index);
  setImpl(this$static.array, index, o);
  return previous;
}

function add_5(index, o){
  if (index < 0 || index > this.size) {
    $indexOutOfBounds(this, index);
  }
  addImpl(this.array, index, o);
  ++this.size;
}

function add_6(o){
  return $add_9(this, o);
}

function addAll_0(c){
  return $addAll(this, c);
}

function addImpl(array, index, o){
  array.splice(index, 0, o);
}

function contains_3(o){
  return $contains_0(this, o);
}

function equals_8(a, b){
  return a === b || a !== null && a.equals$(b);
}

function get_2(index){
  return $get_0(this, index);
}

function getImpl(array, index){
  return array[index];
}

function remove_15(index){
  return $remove_12(this, index);
}

function removeRangeImpl(array, index, count){
  array.splice(index, count);
}

function setImpl(array, index, o){
  array[index] = o;
}

function size_3(){
  return this.size;
}

function toArray_1(a){
  var i;
  if (a.length_0 < this.size) {
    a = clonify_0(a, this.size);
  }
  for (i = 0; i < this.size; ++i) {
    setCheck(a, i, getImpl(this.array, i));
  }
  if (a.length_0 > this.size) {
    setCheck(a, this.size, null);
  }
  return a;
}

function ArrayList(){
}

_ = ArrayList.prototype = new AbstractList();
_.add = add_5;
_.add_0 = add_6;
_.addAll = addAll_0;
_.contains = contains_3;
_.get = get_2;
_.remove = remove_15;
_.size_0 = size_3;
_.toArray_0 = toArray_1;
_.typeName$ = package_java_util_ + 'ArrayList';
_.typeId$ = 72;
_.array = null;
_.size = 0;
function $ChangeListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireChange(this$static, sender){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 12);
    listener.onChange(sender);
  }
}

function ChangeListenerCollection(){
}

_ = ChangeListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ChangeListenerCollection';
_.typeId$ = 73;
function $clinit_67(){
  $clinit_67 = nullMethod;
  $clinit_217() , implWidget;
}

function $CheckBox(this$static){
  $clinit_217() , implWidget;
  $CheckBox_0(this$static, createInputCheck());
  $setStyleName(this$static, 'gwt-CheckBox');
  return this$static;
}

function $CheckBox_1(this$static, label){
  $clinit_217() , implWidget;
  $CheckBox(this$static);
  $setText(this$static, label);
  return this$static;
}

function $CheckBox_0(this$static, elem){
  var uid;
  $clinit_217() , implWidget;
  $ButtonBase(this$static, createSpan());
  this$static.inputElem = elem;
  this$static.labelElem = createLabel();
  sinkEvents(this$static.inputElem, getEventsSunk(this$static.getElement()));
  sinkEvents(this$static.getElement(), 0);
  appendChild(this$static.getElement(), this$static.inputElem);
  appendChild(this$static.getElement(), this$static.labelElem);
  uid = 'check' + ++uniqueId;
  setElementProperty(this$static.inputElem, 'id', uid);
  setElementProperty(this$static.labelElem, 'htmlFor', uid);
  return this$static;
}

function $isChecked(this$static){
  var propName;
  propName = this$static.isAttached()?'checked':'defaultChecked';
  return getElementPropertyBoolean(this$static.inputElem, propName);
}

function $setChecked(this$static, checked){
  setElementPropertyBoolean(this$static.inputElem, 'checked', checked);
  setElementPropertyBoolean(this$static.inputElem, 'defaultChecked', checked);
}

function $setEnabled(this$static, enabled){
  setElementPropertyBoolean(this$static.inputElem, 'disabled', !enabled);
}

function $setText(this$static, text){
  setInnerText(this$static.labelElem, text);
}

function isEnabled(){
  return !getElementPropertyBoolean(this.inputElem, 'disabled');
}

function onLoad(){
  setEventListener(this.inputElem, this);
}

function onUnload(){
  setEventListener(this.inputElem, null);
  $setChecked(this, $isChecked(this));
}

function setEnabled(enabled){
  $setEnabled(this, enabled);
}

function setHTML_0(html){
  setInnerHTML(this.labelElem, html);
}

function CheckBox(){
}

_ = CheckBox.prototype = new ButtonBase();
_.isEnabled = isEnabled;
_.onLoad = onLoad;
_.onUnload = onUnload;
_.setEnabled = setEnabled;
_.setHTML = setHTML_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'CheckBox';
_.typeId$ = 74;
_.inputElem = null;
_.labelElem = null;
var uniqueId = 0;
function $ClickListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireClick(this$static, sender){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 13);
    listener.onClick_0(sender);
  }
}

function ClickListenerCollection(){
}

_ = ClickListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ClickListenerCollection';
_.typeId$ = 75;
function $clinit_74(){
  $clinit_74 = nullMethod;
  $clinit_217() , implWidget;
}

function $CustomButton_0(this$static, upImage){
  $clinit_217() , implWidget;
  $CustomButton(this$static);
  $setImage(this$static.up, upImage);
  return this$static;
}

function $CustomButton(this$static){
  $clinit_217() , implWidget;
  $ButtonBase(this$static, $createFocusable(($clinit_93() , impl_1)));
  $sinkEvents_0(this$static, 6269);
  $setUpFace(this$static, $createFace(this$static, null, 'up', 0));
  $setStyleName(this$static, 'gwt-CustomButton');
  return this$static;
}

function $cleanupCaptureState(this$static){
  if (this$static.isCapturing || this$static.isFocusing) {
    releaseCapture(this$static.getElement());
    this$static.isCapturing = false;
    this$static.isFocusing = false;
    this$static.onClickCancel();
  }
}

function $createFace(this$static, delegateTo, name, faceID){
  return $CustomButton$1(new CustomButton$1(), delegateTo, this$static, name, faceID);
}

function $finishSetup(this$static){
  if (this$static.curFace === null) {
    $setCurrentFace_0(this$static, this$static.up);
  }
}

function $getCurrentFace(this$static){
  $finishSetup(this$static);
  return this$static.curFace;
}

function $getDownDisabledFace(this$static){
  if (this$static.downDisabled === null) {
    $setDownDisabledFace(this$static, $createFace(this$static, $getDownFace(this$static), 'down-disabled', 5));
  }
  return this$static.downDisabled;
}

function $getDownFace(this$static){
  if (this$static.down === null) {
    $setDownFace(this$static, $createFace(this$static, this$static.up, 'down', 1));
  }
  return this$static.down;
}

function $getDownHoveringFace(this$static){
  if (this$static.downHovering === null) {
    $setDownHoveringFace(this$static, $createFace(this$static, $getDownFace(this$static), 'down-hovering', 3));
  }
  return this$static.downHovering;
}

function $getFaceFromID(this$static, id){
  switch (id) {
    case 1:
      return $getDownFace(this$static);
    case 0:
      return this$static.up;
    case 3:
      return $getDownHoveringFace(this$static);
    case 2:
      return $getUpHoveringFace(this$static);
    case 4:
      return $getUpDisabledFace(this$static);
    case 5:
      return $getDownDisabledFace(this$static);
    default:throw $IllegalStateException(new IllegalStateException(), id + ' is not a known face id.');
  }
}

function $getUpDisabledFace(this$static){
  if (this$static.upDisabled === null) {
    $setUpDisabledFace(this$static, $createFace(this$static, this$static.up, 'up-disabled', 4));
  }
  return this$static.upDisabled;
}

function $getUpHoveringFace(this$static){
  if (this$static.upHovering === null) {
    $setUpHoveringFace(this$static, $createFace(this$static, this$static.up, 'up-hovering', 2));
  }
  return this$static.upHovering;
}

function $isDown(this$static){
  return (1 & $getCurrentFace(this$static).val$faceID) > 0;
}

function $isHovering(this$static){
  return (2 & $getCurrentFace(this$static).val$faceID) > 0;
}

function $onClick(this$static){
  $fireClickListeners(this$static);
}

function $setCurrentFace_0(this$static, newFace){
  if (this$static.curFace !== newFace) {
    if (this$static.curFace !== null) {
      $removeStyleDependentName(this$static, this$static.curFace.val$name);
    }
    this$static.curFace = newFace;
    $setCurrentFaceElement(this$static, $getFace(newFace));
    $addStyleDependentName(this$static, this$static.curFace.val$name);
  }
}

function $setCurrentFace(this$static, faceID){
  var newFace;
  newFace = $getFaceFromID(this$static, faceID);
  $setCurrentFace_0(this$static, newFace);
}

function $setCurrentFaceElement(this$static, newFaceElement){
  if (this$static.curFaceElement !== newFaceElement) {
    if (this$static.curFaceElement !== null) {
      removeChild(this$static.getElement(), this$static.curFaceElement);
    }
    this$static.curFaceElement = newFaceElement;
    appendChild(this$static.getElement(), this$static.curFaceElement);
  }
}

function $setDown(this$static, down){
  if (down != this$static.isDown()) {
    $toggleDown(this$static);
  }
}

function $setDownDisabledFace(this$static, downDisabled){
  this$static.downDisabled = downDisabled;
}

function $setDownFace(this$static, down){
  this$static.down = down;
}

function $setDownHoveringFace(this$static, downHovering){
  this$static.downHovering = downHovering;
}

function $setFocus(this$static, focused){
  if (focused) {
    $focus(($clinit_93() , impl_1), this$static.getElement());
  }
   else {
    $blur_0(($clinit_93() , impl_1), this$static.getElement());
  }
}

function $setHovering(this$static, hovering){
  if (hovering != $isHovering(this$static)) {
    $toggleHover(this$static);
  }
}

function $setUpDisabledFace(this$static, upDisabled){
  this$static.upDisabled = upDisabled;
}

function $setUpFace(this$static, up){
  this$static.up = up;
}

function $setUpHoveringFace(this$static, upHovering){
  this$static.upHovering = upHovering;
}

function $toggleDisabled(this$static){
  var newFaceID;
  newFaceID = $getCurrentFace(this$static).val$faceID ^ 4;
  newFaceID &= (-3);
  $setCurrentFace(this$static, newFaceID);
}

function $toggleDown(this$static){
  var newFaceID;
  newFaceID = $getCurrentFace(this$static).val$faceID ^ 1;
  $setCurrentFace(this$static, newFaceID);
}

function $toggleHover(this$static){
  var newFaceID;
  newFaceID = $getCurrentFace(this$static).val$faceID ^ 2;
  newFaceID &= (-5);
  $setCurrentFace(this$static, newFaceID);
}

function isDown(){
  return $isDown(this);
}

function onAttach_0(){
  $finishSetup(this);
  $onAttach(this);
}

function onBrowserEvent_0(event_0){
  var keyCode, type;
  if (this.isEnabled() == false) {
    return;
  }
  type = eventGetType(event_0);
  switch (type) {
    case 4:
      $setFocus(this, true);
      this.onClickStart();
      setCapture(this.getElement());
      this.isCapturing = true;
      eventPreventDefault(event_0);
      break;
    case 8:
      if (this.isCapturing) {
        this.isCapturing = false;
        releaseCapture(this.getElement());
        if ($isHovering(this)) {
          this.onClick();
        }
      }

      break;
    case 64:
      if (this.isCapturing) {
        eventPreventDefault(event_0);
      }

      break;
    case 32:
      if (isOrHasChild(this.getElement(), eventGetTarget(event_0)) && !isOrHasChild(this.getElement(), eventGetToElement(event_0))) {
        if (this.isCapturing) {
          this.onClickCancel();
        }
        $setHovering(this, false);
      }

      break;
    case 16:
      if (isOrHasChild(this.getElement(), eventGetTarget(event_0))) {
        $setHovering(this, true);
        if (this.isCapturing) {
          this.onClickStart();
        }
      }

      break;
    case 1:
      return;
    case 4096:
      if (this.isFocusing) {
        this.isFocusing = false;
        this.onClickCancel();
      }

      break;
    case 8192:
      if (this.isCapturing) {
        this.isCapturing = false;
        this.onClickCancel();
      }

      break;
  }
  $onBrowserEvent(this, event_0);
  keyCode = narrow_char(eventGetKeyCode(event_0));
  switch (type) {
    case 128:
      if (keyCode == 32) {
        this.isFocusing = true;
        this.onClickStart();
      }

      break;
    case 512:
      if (this.isFocusing && keyCode == 32) {
        this.isFocusing = false;
        this.onClick();
      }

      break;
    case 256:
      if (keyCode == 10 || keyCode == 13) {
        this.onClickStart();
        this.onClick();
      }

      break;
  }
}

function onClick_3(){
  $onClick(this);
}

function onClickCancel(){
}

function onClickStart(){
}

function onDetach_0(){
  $onDetach(this);
  $cleanupCaptureState(this);
}

function setDown(down){
  $setDown(this, down);
}

function setEnabled_0(enabled){
  if (this.isEnabled() != enabled) {
    $toggleDisabled(this);
    $setEnabled_0(this, enabled);
    if (!enabled) {
      $cleanupCaptureState(this);
    }
  }
}

function setHTML_1(html){
  $setHTML($getCurrentFace(this), html);
}

function CustomButton(){
}

_ = CustomButton.prototype = new ButtonBase();
_.isDown = isDown;
_.onAttach = onAttach_0;
_.onBrowserEvent = onBrowserEvent_0;
_.onClick = onClick_3;
_.onClickCancel = onClickCancel;
_.onClickStart = onClickStart;
_.onDetach = onDetach_0;
_.setDown = setDown;
_.setEnabled = setEnabled_0;
_.setHTML = setHTML_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'CustomButton';
_.typeId$ = 76;
_.curFace = null;
_.curFaceElement = null;
_.down = null;
_.downDisabled = null;
_.downHovering = null;
_.isCapturing = false;
_.isFocusing = false;
_.up = null;
_.upDisabled = null;
_.upHovering = null;
function $CustomButton$Face(this$static, delegateTo, this$0){
  this$static.this$0 = this$0;
  this$static.delegateTo = delegateTo;
  return this$static;
}

function $getFace(this$static){
  if (this$static.face === null) {
    if (this$static.delegateTo === null) {
      this$static.face = createDiv();
      return this$static.face;
    }
     else {
      return $getFace(this$static.delegateTo);
    }
  }
   else {
    return this$static.face;
  }
}

function $setHTML(this$static, html){
  this$static.face = createDiv();
  setStyleName_0(this$static.face, 'html-face', true);
  setInnerHTML(this$static.face, html);
  $updateButtonFace(this$static);
}

function $setImage(this$static, image){
  this$static.face = image.getElement();
  $updateButtonFace(this$static);
}

function $updateButtonFace(this$static){
  if (this$static.this$0.curFace !== null && $getFace(this$static.this$0.curFace) === $getFace(this$static)) {
    $setCurrentFaceElement(this$static.this$0, this$static.face);
  }
}

function toString_4(){
  return this.getName();
}

function CustomButton$Face(){
}

_ = CustomButton$Face.prototype = new Object_0();
_.toString$ = toString_4;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'CustomButton$Face';
_.typeId$ = 77;
_.delegateTo = null;
_.face = null;
function $CustomButton$1(this$static, $anonymous0, this$0, val$name, val$faceID){
  this$static.val$name = val$name;
  this$static.val$faceID = val$faceID;
  $CustomButton$Face(this$static, $anonymous0, this$0);
  return this$static;
}

function getName(){
  return this.val$name;
}

function CustomButton$1(){
}

_ = CustomButton$1.prototype = new CustomButton$Face();
_.getName = getName;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'CustomButton$1';
_.typeId$ = 78;
function $DeckPanel(this$static){
  $ComplexPanel(this$static);
  this$static.setElement(createDiv());
  return this$static;
}

function $initChildWidget(this$static, w){
  var child;
  child = w.getElement();
  setStyleAttribute(child, 'width', '100%');
  setStyleAttribute(child, 'height', '100%');
  w.setVisible(false);
}

function $insert_0(this$static, w, beforeIndex){
  $insert(this$static, w, this$static.getElement(), beforeIndex, true);
  $initChildWidget(this$static, w);
}

function $remove_2(this$static, w){
  var removed;
  removed = $remove_1(this$static, w);
  if (removed) {
    $resetChildWidget(this$static, w);
    if (this$static.visibleWidget === w) {
      this$static.visibleWidget = null;
    }
  }
  return removed;
}

function $resetChildWidget(this$static, w){
  setStyleAttribute(w.getElement(), 'width', '');
  setStyleAttribute(w.getElement(), 'height', '');
  w.setVisible(true);
}

function $showWidget(this$static, index){
  $checkIndexBoundsForAccess(this$static, index);
  if (this$static.visibleWidget !== null) {
    this$static.visibleWidget.setVisible(false);
  }
  this$static.visibleWidget = $getWidget(this$static, index);
  this$static.visibleWidget.setVisible(true);
}

function remove_2(w){
  return $remove_2(this, w);
}

function DeckPanel(){
}

_ = DeckPanel.prototype = new ComplexPanel();
_.remove_0 = remove_2;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DeckPanel';
_.typeId$ = 79;
_.visibleWidget = null;
function EventObject(){
}

_ = EventObject.prototype = new Object_0();
_.typeName$ = package_java_util_ + 'EventObject';
_.typeId$ = 80;
function DisclosureEvent(){
}

_ = DisclosureEvent.prototype = new EventObject();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DisclosureEvent';
_.typeId$ = 81;
function $$init_9(this$static){
  this$static.mainPanel = $VerticalPanel(new VerticalPanel());
  this$static.header = $DisclosurePanel$ClickableHeader(new DisclosurePanel$ClickableHeader(), this$static);
}

function $DisclosurePanel(this$static, images, headerText, isOpen){
  $$init_9(this$static);
  $init_2(this$static, isOpen);
  $setHeader(this$static, $DisclosurePanel$DefaultHeader(new DisclosurePanel$DefaultHeader(), images, headerText, this$static));
  return this$static;
}

function $DisclosurePanel_0(this$static, headerText){
  $DisclosurePanel(this$static, createDefaultImages(), headerText, false);
  return this$static;
}

function $addEventHandler(this$static, handler){
  if (this$static.handlers === null) {
    this$static.handlers = $ArrayList(new ArrayList());
  }
  $add_9(this$static.handlers, handler);
}

function $fireEvent(this$static){
  var event_0, handler, it;
  if (this$static.handlers === null) {
    return;
  }
  event_0 = new DisclosureEvent();
  for (it = $iterator_3(this$static.handlers); $hasNext_4(it);) {
    handler = dynamicCast($next_3(it), 14);
    if (this$static.isOpen) {
      handler.onOpen(event_0);
    }
     else {
      handler.onClose(event_0);
    }
  }
}

function $init_2(this$static, isOpen){
  $initWidget(this$static, this$static.mainPanel);
  $add_7(this$static.mainPanel, this$static.header);
  this$static.isOpen = isOpen;
  $setStyleName(this$static, 'gwt-DisclosurePanel');
  $setContentDisplay(this$static);
}

function $setContent(this$static, content){
  var currentContent;
  currentContent = this$static.content;
  if (currentContent !== null) {
    $remove_7(this$static.mainPanel, currentContent);
    $removeStyleName(currentContent, 'content');
  }
  this$static.content = content;
  if (content !== null) {
    $add_7(this$static.mainPanel, content);
    $addStyleName(content, 'content');
    $setContentDisplay(this$static);
  }
}

function $setContentDisplay(this$static){
  if (this$static.isOpen) {
    $removeStyleDependentName(this$static, 'closed');
    $addStyleDependentName(this$static, 'open');
  }
   else {
    $removeStyleDependentName(this$static, 'open');
    $addStyleDependentName(this$static, 'closed');
  }
  if (this$static.content !== null) {
    this$static.content.setVisible(this$static.isOpen);
  }
}

function $setHeader(this$static, headerWidget){
  this$static.header.setWidget(headerWidget);
}

function $setOpen(this$static, isOpen){
  if (this$static.isOpen != isOpen) {
    this$static.isOpen = isOpen;
    $setContentDisplay(this$static);
    $fireEvent(this$static);
  }
}

function createDefaultImages(){
  return $DisclosurePanelImages_generatedBundle(new DisclosurePanelImages_generatedBundle());
}

function iterator_0(){
  return createWidgetIterator(this, initValues('[Lcom.google.gwt.user.client.ui.Widget;', 204, 15, [this.content]));
}

function remove_4(w){
  if (w === this.content) {
    $setContent(this, null);
    return true;
  }
  return false;
}

function DisclosurePanel(){
}

_ = DisclosurePanel.prototype = new Composite();
_.iterator_0 = iterator_0;
_.remove_0 = remove_4;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DisclosurePanel';
_.typeId$ = 82;
_.content = null;
_.handlers = null;
_.isOpen = false;
function $DisclosurePanel$ClickableHeader(this$static, this$0){
  var elem;
  this$static.this$0 = this$0;
  $SimplePanel_0(this$static, createAnchor());
  elem = this$static.getElement();
  setElementProperty(elem, 'href', 'javascript:void(0);');
  setStyleAttribute(elem, 'display', 'block');
  $sinkEvents_0(this$static, 1);
  $setStyleName(this$static, 'header');
  return this$static;
}

function onBrowserEvent_1(event_0){
  switch (eventGetType(event_0)) {
    case 1:
      eventPreventDefault(event_0);
      $setOpen(this.this$0, !this.this$0.isOpen);
  }
}

function DisclosurePanel$ClickableHeader(){
}

_ = DisclosurePanel$ClickableHeader.prototype = new SimplePanel();
_.onBrowserEvent = onBrowserEvent_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DisclosurePanel$ClickableHeader';
_.typeId$ = 83;
function $DisclosurePanel$DefaultHeader(this$static, images, text, this$0){
  var imageTD, root, tbody, tr;
  this$static.this$0 = this$0;
  this$static.iconImage = this$static.this$0.isOpen?$createImage(($clinit_82() , disclosurePanelOpen_SINGLETON)):$createImage(($clinit_82() , disclosurePanelClosed_SINGLETON));
  root = createTable();
  tbody = createTBody();
  tr = createTR();
  imageTD = createTD();
  this$static.labelTD = createTD();
  this$static.setElement(root);
  appendChild(root, tbody);
  appendChild(tbody, tr);
  appendChild(tr, imageTD);
  appendChild(tr, this$static.labelTD);
  setElementProperty(imageTD, 'align', 'center');
  setElementProperty(imageTD, 'valign', 'middle');
  setStyleAttribute(imageTD, 'width', $getWidth(this$static.iconImage) + 'px');
  appendChild(imageTD, this$static.iconImage.getElement());
  $setText_1(this$static, text);
  $addEventHandler(this$static.this$0, this$static);
  $setStyle(this$static);
  return this$static;
}

function $setStyle(this$static){
  if (this$static.this$0.isOpen) {
    $applyTo(($clinit_82() , disclosurePanelOpen_SINGLETON), this$static.iconImage);
  }
   else {
    $applyTo(($clinit_82() , disclosurePanelClosed_SINGLETON), this$static.iconImage);
  }
}

function $setText_1(this$static, text){
  setInnerText(this$static.labelTD, text);
}

function onClose(event_0){
  $setStyle(this);
}

function onOpen(event_0){
  $setStyle(this);
}

function DisclosurePanel$DefaultHeader(){
}

_ = DisclosurePanel$DefaultHeader.prototype = new Widget();
_.onClose = onClose;
_.onOpen = onOpen;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DisclosurePanel$DefaultHeader';
_.typeId$ = 84;
_.iconImage = null;
_.labelTD = null;
function $clinit_82(){
  $clinit_82 = nullMethod;
  IMAGE_BUNDLE_URL_1 = getModuleBaseURL() + 'FE331E1C8EFF24F8BD692603EDFEDBF3.cache.png';
  disclosurePanelClosed_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL_1, 0, 0, 16, 16);
  disclosurePanelOpen_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL_1, 16, 0, 16, 16);
}

function $DisclosurePanelImages_generatedBundle(this$static){
  $clinit_82();
  return this$static;
}

function DisclosurePanelImages_generatedBundle(){
}

_ = DisclosurePanelImages_generatedBundle.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DisclosurePanelImages_generatedBundle';
_.typeId$ = 85;
var IMAGE_BUNDLE_URL_1, disclosurePanelClosed_SINGLETON, disclosurePanelOpen_SINGLETON;
function $clinit_87(){
  $clinit_87 = nullMethod;
  CENTER = new DockPanel$DockLayoutConstant();
  EAST = new DockPanel$DockLayoutConstant();
  NORTH = new DockPanel$DockLayoutConstant();
  SOUTH = new DockPanel$DockLayoutConstant();
  WEST = new DockPanel$DockLayoutConstant();
}

function $$init_10(this$static){
  this$static.horzAlign = ($clinit_108() , ALIGN_LEFT);
  this$static.vertAlign = ($clinit_112() , ALIGN_TOP);
}

function $DockPanel(this$static){
  $clinit_87();
  $CellPanel(this$static);
  $$init_10(this$static);
  setElementPropertyInt(this$static.table, 'cellSpacing', 0);
  setElementPropertyInt(this$static.table, 'cellPadding', 0);
  return this$static;
}

function $add_1(this$static, widget, direction){
  var layout;
  if (direction === CENTER) {
    if (widget === this$static.center) {
      return;
    }
     else if (this$static.center !== null) {
      throw $IllegalArgumentException(new IllegalArgumentException(), 'Only one CENTER widget may be added');
    }
  }
  $removeFromParent(widget);
  $add_8(this$static.children_0, widget);
  if (direction === CENTER) {
    this$static.center = widget;
  }
  layout = $DockPanel$LayoutData(new DockPanel$LayoutData(), direction);
  $setLayoutData(widget, layout);
  $setCellHorizontalAlignment_0(this$static, widget, this$static.horzAlign);
  $setCellVerticalAlignment_0(this$static, widget, this$static.vertAlign);
  $realizeTable(this$static);
  $adopt(this$static, widget);
}

function $realizeTable(this$static){
  var bodyElem, centerTd, child, colCount, dir, eastCol, i, it, layout, northRow, row, rowCount, rows, southRow, td, westCol;
  bodyElem = this$static.body_0;
  while (getChildCount(bodyElem) > 0) {
    removeChild(bodyElem, getChild(bodyElem, 0));
  }
  rowCount = 1;
  colCount = 1;
  for (it = $iterator_2(this$static.children_0); $hasNext_2(it);) {
    child = $next_1(it);
    dir = child.layoutData.direction;
    if (dir === NORTH || dir === SOUTH) {
      ++rowCount;
    }
     else if (dir === EAST || dir === WEST) {
      ++colCount;
    }
  }
  rows = initDims_0('[Lcom.google.gwt.user.client.ui.DockPanel$TmpRow;', [203], [37], [rowCount], null);
  for (i = 0; i < rowCount; ++i) {
    rows[i] = new DockPanel$TmpRow();
    rows[i].tr = createTR();
    appendChild(bodyElem, rows[i].tr);
  }
  westCol = 0;
  eastCol = colCount - 1;
  northRow = 0;
  southRow = rowCount - 1;
  centerTd = null;
  for (it = $iterator_2(this$static.children_0); $hasNext_2(it);) {
    child = $next_1(it);
    layout = child.layoutData;
    td = createTD();
    layout.td = td;
    setElementProperty(layout.td, 'align', layout.hAlign);
    setStyleAttribute(layout.td, 'verticalAlign', layout.vAlign);
    setElementProperty(layout.td, 'width', layout.width_0);
    setElementProperty(layout.td, 'height', layout.height_0);
    if (layout.direction === NORTH) {
      insertChild(rows[northRow].tr, td, rows[northRow].center);
      appendChild(td, child.getElement());
      setElementPropertyInt(td, 'colSpan', eastCol - westCol + 1);
      ++northRow;
    }
     else if (layout.direction === SOUTH) {
      insertChild(rows[southRow].tr, td, rows[southRow].center);
      appendChild(td, child.getElement());
      setElementPropertyInt(td, 'colSpan', eastCol - westCol + 1);
      --southRow;
    }
     else if (layout.direction === WEST) {
      row = rows[northRow];
      insertChild(row.tr, td, row.center++);
      appendChild(td, child.getElement());
      setElementPropertyInt(td, 'rowSpan', southRow - northRow + 1);
      ++westCol;
    }
     else if (layout.direction === EAST) {
      row = rows[northRow];
      insertChild(row.tr, td, row.center);
      appendChild(td, child.getElement());
      setElementPropertyInt(td, 'rowSpan', southRow - northRow + 1);
      --eastCol;
    }
     else if (layout.direction === CENTER) {
      centerTd = td;
    }
  }
  if (this$static.center !== null) {
    row = rows[northRow];
    insertChild(row.tr, centerTd, row.center);
    appendChild(centerTd, this$static.center.getElement());
  }
}

function $setCellHorizontalAlignment_0(this$static, w, align){
  var data;
  data = w.layoutData;
  data.hAlign = align.textAlignString;
  if (data.td !== null) {
    setElementProperty(data.td, 'align', data.hAlign);
  }
}

function $setCellVerticalAlignment_0(this$static, w, align){
  var data;
  data = w.layoutData;
  data.vAlign = align.verticalAlignString;
  if (data.td !== null) {
    setStyleAttribute(data.td, 'verticalAlign', data.vAlign);
  }
}

function $setHorizontalAlignment(this$static, align){
  this$static.horzAlign = align;
}

function remove_5(w){
  var removed;
  removed = $remove_1(this, w);
  if (removed) {
    if (w === this.center) {
      this.center = null;
    }
    $realizeTable(this);
  }
  return removed;
}

function setCellHeight_0(w, height){
  var data;
  data = w.layoutData;
  data.height_0 = height;
  if (data.td !== null) {
    setStyleAttribute(data.td, 'height', data.height_0);
  }
}

function setCellHorizontalAlignment_0(w, align){
  $setCellHorizontalAlignment_0(this, w, align);
}

function setCellVerticalAlignment_0(w, align){
  $setCellVerticalAlignment_0(this, w, align);
}

function setCellWidth_0(w, width){
  var data;
  data = w.layoutData;
  data.width_0 = width;
  if (data.td !== null) {
    setStyleAttribute(data.td, 'width', data.width_0);
  }
}

function DockPanel(){
}

_ = DockPanel.prototype = new CellPanel();
_.remove_0 = remove_5;
_.setCellHeight = setCellHeight_0;
_.setCellHorizontalAlignment = setCellHorizontalAlignment_0;
_.setCellVerticalAlignment = setCellVerticalAlignment_0;
_.setCellWidth = setCellWidth_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DockPanel';
_.typeId$ = 86;
_.center = null;
var CENTER, EAST, NORTH, SOUTH, WEST;
function DockPanel$DockLayoutConstant(){
}

_ = DockPanel$DockLayoutConstant.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DockPanel$DockLayoutConstant';
_.typeId$ = 87;
function $DockPanel$LayoutData(this$static, dir){
  this$static.direction = dir;
  return this$static;
}

function DockPanel$LayoutData(){
}

_ = DockPanel$LayoutData.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DockPanel$LayoutData';
_.typeId$ = 88;
_.direction = null;
_.hAlign = 'left';
_.height_0 = '';
_.td = null;
_.vAlign = 'top';
_.width_0 = '';
function DockPanel$TmpRow(){
}

_ = DockPanel$TmpRow.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DockPanel$TmpRow';
_.typeId$ = 89;
_.center = 0;
_.tr = null;
function $$init_13(this$static){
  this$static.widgetMap = $HTMLTable$WidgetMapper(new HTMLTable$WidgetMapper());
}

function $HTMLTable(this$static){
  $$init_13(this$static);
  this$static.tableElem = createTable();
  this$static.bodyElem = createTBody();
  appendChild(this$static.tableElem, this$static.bodyElem);
  this$static.setElement(this$static.tableElem);
  $sinkEvents_0(this$static, 1);
  return this$static;
}

function $checkCellBounds(this$static, row, column){
  var cellSize;
  $checkRowBounds(this$static, row);
  if (column < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Column ' + column + ' must be non-negative: ' + column);
  }
  cellSize = this$static.getCellCount(row);
  if (cellSize <= column) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Column index: ' + column + ', Column size: ' + this$static.getCellCount(row));
  }
}

function $checkRowBounds(this$static, row){
  var rowSize;
  rowSize = this$static.getRowCount();
  if (row >= rowSize || row < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Row index: ' + row + ', Row size: ' + rowSize);
  }
}

function $cleanCell(this$static, row, column, clearInnerHTML){
  var td;
  td = $getRawElement(this$static.cellFormatter, row, column);
  $internalClearCell(this$static, td, clearInnerHTML);
  return td;
}

function $createCell(this$static){
  return createTD();
}

function $getDOMCellCount(this$static, tableBody, row){
  return tableBody.rows[row].cells.length;
}

function $getDOMRowCount(this$static){
  return $getDOMRowCount_0(this$static, this$static.bodyElem);
}

function $getDOMRowCount_0(this$static, elem){
  return elem.rows.length;
}

function $insertCell(this$static, row, column){
  var td, tr;
  tr = $getRow(this$static.rowFormatter, this$static.bodyElem, row);
  td = this$static.createCell();
  insertChild(tr, td, column);
}

function $insertRow_0(this$static, beforeRow){
  var tr;
  if (beforeRow != $getRowCount(this$static)) {
    $checkRowBounds(this$static, beforeRow);
  }
  tr = createTR();
  insertChild(this$static.bodyElem, tr, beforeRow);
  return beforeRow;
}

function $internalClearCell(this$static, td, clearInnerHTML){
  var maybeChild, widget;
  maybeChild = getFirstChild(td);
  widget = null;
  if (maybeChild !== null) {
    widget = $getWidget_0(this$static.widgetMap, maybeChild);
  }
  if (widget !== null) {
    $remove_3(this$static, widget);
    return true;
  }
   else {
    if (clearInnerHTML) {
      setInnerHTML(td, '');
    }
    return false;
  }
}

function $remove_3(this$static, widget){
  var elem;
  if (widget.parent !== this$static) {
    return false;
  }
  $orphan(this$static, widget);
  elem = widget.getElement();
  removeChild(getParent(elem), elem);
  $removeWidgetByElement(this$static.widgetMap, elem);
  return true;
}

function $removeCell(this$static, row, column){
  var td, tr;
  $checkCellBounds(this$static, row, column);
  td = $cleanCell(this$static, row, column, false);
  tr = $getRow(this$static.rowFormatter, this$static.bodyElem, row);
  removeChild(tr, td);
}

function $removeRow(this$static, row){
  var column, columnCount;
  columnCount = this$static.getCellCount(row);
  for (column = 0; column < columnCount; ++column) {
    $cleanCell(this$static, row, column, false);
  }
  removeChild(this$static.bodyElem, $getRow(this$static.rowFormatter, this$static.bodyElem, row));
}

function $setBorderWidth(this$static, width){
  setElementProperty(this$static.tableElem, 'border', '' + width);
}

function $setCellFormatter(this$static, cellFormatter){
  this$static.cellFormatter = cellFormatter;
}

function $setCellPadding(this$static, padding){
  setElementPropertyInt(this$static.tableElem, 'cellPadding', padding);
}

function $setCellSpacing(this$static, spacing){
  setElementPropertyInt(this$static.tableElem, 'cellSpacing', spacing);
}

function $setColumnFormatter(this$static, formatter){
  this$static.columnFormatter = formatter;
  $prepareColumnGroup(this$static.columnFormatter);
}

function $setRowFormatter(this$static, rowFormatter){
  this$static.rowFormatter = rowFormatter;
}

function $setWidget_0(this$static, row, column, widget){
  var td;
  this$static.prepareCell(row, column);
  if (widget !== null) {
    $removeFromParent(widget);
    td = $cleanCell(this$static, row, column, true);
    $putWidget(this$static.widgetMap, widget);
    appendChild(td, widget.getElement());
    $adopt(this$static, widget);
  }
}

function createCell_0(){
  return $createCell(this);
}

function insertCell_0(row, column){
  $insertCell(this, row, column);
}

function iterator_1(){
  return $widgetIterator(this.widgetMap);
}

function onBrowserEvent_3(event_0){
  switch (eventGetType(event_0)) {
    case 1:
      {
        break;
      }

    default:}
}

function remove_6(widget){
  return $remove_3(this, widget);
}

function removeCell_0(row, column){
  $removeCell(this, row, column);
}

function removeRow_0(row){
  $removeRow(this, row);
}

function HTMLTable(){
}

_ = HTMLTable.prototype = new Panel();
_.createCell = createCell_0;
_.insertCell = insertCell_0;
_.iterator_0 = iterator_1;
_.onBrowserEvent = onBrowserEvent_3;
_.remove_0 = remove_6;
_.removeCell = removeCell_0;
_.removeRow = removeRow_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable';
_.typeId$ = 90;
_.bodyElem = null;
_.cellFormatter = null;
_.columnFormatter = null;
_.rowFormatter = null;
_.tableElem = null;
function $FlexTable(this$static){
  $HTMLTable(this$static);
  $setCellFormatter(this$static, $FlexTable$FlexCellFormatter(new FlexTable$FlexCellFormatter(), this$static));
  $setRowFormatter(this$static, new HTMLTable$RowFormatter());
  $setColumnFormatter(this$static, $HTMLTable$ColumnFormatter(new HTMLTable$ColumnFormatter(), this$static));
  return this$static;
}

function $getCellCount(this$static, row){
  $checkRowBounds(this$static, row);
  return $getDOMCellCount(this$static, this$static.bodyElem, row);
}

function $getRowCount(this$static){
  return $getDOMRowCount(this$static);
}

function $insertRow(this$static, beforeRow){
  return $insertRow_0(this$static, beforeRow);
}

function $prepareRow(this$static, row){
  var i, rowCount;
  if (row < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Cannot create a row with a negative index: ' + row);
  }
  rowCount = $getRowCount(this$static);
  for (i = rowCount; i <= row; i++) {
    $insertRow(this$static, i);
  }
}

function addCells(table, row, num){
  var rowElem = table.rows[row];
  for (var i = 0; i < num; i++) {
    var cell = $doc.createElement('td');
    rowElem.appendChild(cell);
  }
}

function getCellCount(row){
  return $getCellCount(this, row);
}

function getRowCount(){
  return $getRowCount(this);
}

function insertCell(beforeRow, beforeColumn){
  $insertCell(this, beforeRow, beforeColumn);
}

function prepareCell(row, column){
  var cellCount, required;
  $prepareRow(this, row);
  if (column < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Cannot create a column with a negative index: ' + column);
  }
  cellCount = $getCellCount(this, row);
  required = column + 1 - cellCount;
  if (required > 0) {
    addCells(this.bodyElem, row, required);
  }
}

function removeCell(row, col){
  $removeCell(this, row, col);
}

function removeRow(row){
  $removeRow(this, row);
}

function FlexTable(){
}

_ = FlexTable.prototype = new HTMLTable();
_.getCellCount = getCellCount;
_.getRowCount = getRowCount;
_.insertCell = insertCell;
_.prepareCell = prepareCell;
_.removeCell = removeCell;
_.removeRow = removeRow;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'FlexTable';
_.typeId$ = 91;
function $HTMLTable$CellFormatter(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $getCellElement(this$static, table, row, col){
  var out = table.rows[row].cells[col];
  return out == null?null:out;
}

function $getRawElement(this$static, row, column){
  return $getCellElement(this$static, this$static.this$0.bodyElem, row, column);
}

function $setAlignment(this$static, row, column, hAlign, vAlign){
  $setHorizontalAlignment_0(this$static, row, column, hAlign);
  $setVerticalAlignment(this$static, row, column, vAlign);
}

function $setHeight(this$static, row, column, height){
  var elem;
  this$static.this$0.prepareCell(row, column);
  elem = $getCellElement(this$static, this$static.this$0.bodyElem, row, column);
  setElementProperty(elem, 'height', height);
}

function $setHorizontalAlignment_0(this$static, row, column, align){
  var elem;
  this$static.this$0.prepareCell(row, column);
  elem = $getCellElement(this$static, this$static.this$0.bodyElem, row, column);
  setElementProperty(elem, 'align', align.textAlignString);
}

function $setVerticalAlignment(this$static, row, column, align){
  this$static.this$0.prepareCell(row, column);
  setStyleAttribute($getCellElement(this$static, this$static.this$0.bodyElem, row, column), 'verticalAlign', align.verticalAlignString);
}

function $setWidth(this$static, row, column, width){
  this$static.this$0.prepareCell(row, column);
  setElementProperty($getCellElement(this$static, this$static.this$0.bodyElem, row, column), 'width', width);
}

function HTMLTable$CellFormatter(){
}

_ = HTMLTable$CellFormatter.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$CellFormatter';
_.typeId$ = 92;
function $FlexTable$FlexCellFormatter(this$static, this$0){
  $HTMLTable$CellFormatter(this$static, this$0);
  return this$static;
}

function FlexTable$FlexCellFormatter(){
}

_ = FlexTable$FlexCellFormatter.prototype = new HTMLTable$CellFormatter();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'FlexTable$FlexCellFormatter';
_.typeId$ = 93;
function $FlowPanel(this$static){
  $ComplexPanel(this$static);
  this$static.setElement(createDiv());
  return this$static;
}

function $add_2(this$static, w){
  $add_0(this$static, w, this$static.getElement());
}

function FlowPanel(){
}

_ = FlowPanel.prototype = new ComplexPanel();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'FlowPanel';
_.typeId$ = 94;
function $clinit_93(){
  $clinit_93 = nullMethod;
  impl_1 = ($clinit_217() , implPanel);
}

var impl_1;
function $Grid(this$static){
  $HTMLTable(this$static);
  $setCellFormatter(this$static, $HTMLTable$CellFormatter(new HTMLTable$CellFormatter(), this$static));
  $setRowFormatter(this$static, new HTMLTable$RowFormatter());
  $setColumnFormatter(this$static, $HTMLTable$ColumnFormatter(new HTMLTable$ColumnFormatter(), this$static));
  return this$static;
}

function $Grid_0(this$static, rows, columns){
  $Grid(this$static);
  $resize(this$static, rows, columns);
  return this$static;
}

function $prepareRow_0(this$static, row){
  if (row < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Cannot access a row with a negative index: ' + row);
  }
  if (row >= this$static.numRows) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Row index: ' + row + ', Row size: ' + this$static.numRows);
  }
}

function $resize(this$static, rows, columns){
  $resizeColumns(this$static, columns);
  $resizeRows(this$static, rows);
}

function $resizeColumns(this$static, columns){
  var i, j;
  if (this$static.numColumns == columns) {
    return;
  }
  if (columns < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Cannot set number of columns to ' + columns);
  }
  if (this$static.numColumns > columns) {
    for (i = 0; i < this$static.numRows; i++) {
      for (j = this$static.numColumns - 1; j >= columns; j--) {
        this$static.removeCell(i, j);
      }
    }
  }
   else {
    for (i = 0; i < this$static.numRows; i++) {
      for (j = this$static.numColumns; j < columns; j++) {
        this$static.insertCell(i, j);
      }
    }
  }
  this$static.numColumns = columns;
}

function $resizeRows(this$static, rows){
  if (this$static.numRows == rows) {
    return;
  }
  if (rows < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Cannot set number of rows to ' + rows);
  }
  if (this$static.numRows < rows) {
    addRows(this$static.bodyElem, rows - this$static.numRows, this$static.numColumns);
    this$static.numRows = rows;
  }
   else {
    while (this$static.numRows > rows) {
      this$static.removeRow(--this$static.numRows);
    }
  }
}

function addRows(table, rows, columns){
  var td = $doc.createElement('td');
  td.innerHTML = '&nbsp;';
  var row = $doc.createElement('tr');
  for (var cellNum = 0; cellNum < columns; cellNum++) {
    var cell = td.cloneNode(true);
    row.appendChild(cell);
  }
  table.appendChild(row);
  for (var rowNum = 1; rowNum < rows; rowNum++) {
    table.appendChild(row.cloneNode(true));
  }
}

function createCell(){
  var td;
  td = $createCell(this);
  setInnerHTML(td, '&nbsp;');
  return td;
}

function getCellCount_0(row){
  return this.numColumns;
}

function getRowCount_0(){
  return this.numRows;
}

function prepareCell_0(row, column){
  $prepareRow_0(this, row);
  if (column < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Cannot access a column with a negative index: ' + column);
  }
  if (column >= this.numColumns) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Column index: ' + column + ', Column size: ' + this.numColumns);
  }
}

function Grid(){
}

_ = Grid.prototype = new HTMLTable();
_.createCell = createCell;
_.getCellCount = getCellCount_0;
_.getRowCount = getRowCount_0;
_.prepareCell = prepareCell_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Grid';
_.typeId$ = 95;
_.numColumns = 0;
_.numRows = 0;
function $Label(this$static){
  this$static.setElement(createDiv());
  $sinkEvents_0(this$static, 131197);
  $setStyleName(this$static, 'gwt-Label');
  return this$static;
}

function $Label_0(this$static, text){
  $Label(this$static);
  $setText_3(this$static, text);
  return this$static;
}

function $addClickListener(this$static, listener){
  if (this$static.clickListeners === null) {
    this$static.clickListeners = $ClickListenerCollection(new ClickListenerCollection());
  }
  $add_9(this$static.clickListeners, listener);
}

function $addMouseListener(this$static, listener){
  if (this$static.mouseListeners === null) {
    this$static.mouseListeners = $MouseListenerCollection(new MouseListenerCollection());
  }
  $add_9(this$static.mouseListeners, listener);
}

function $setText_3(this$static, text){
  setInnerText(this$static.getElement(), text);
}

function $setWordWrap(this$static, wrap){
  setStyleAttribute(this$static.getElement(), 'whiteSpace', wrap?'normal':'nowrap');
}

function onBrowserEvent_6(event_0){
  switch (eventGetType(event_0)) {
    case 1:
      if (this.clickListeners !== null) {
        $fireClick(this.clickListeners, this);
      }

      break;
    case 4:
    case 8:
    case 64:
    case 16:
    case 32:
      if (this.mouseListeners !== null) {
        $fireMouseEvent(this.mouseListeners, this, event_0);
      }

      break;
    case 131072:
      break;
  }
}

function Label(){
}

_ = Label.prototype = new Widget();
_.onBrowserEvent = onBrowserEvent_6;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Label';
_.typeId$ = 96;
_.clickListeners = null;
_.mouseListeners = null;
function $HTML(this$static){
  $Label(this$static);
  this$static.setElement(createDiv());
  $sinkEvents_0(this$static, 125);
  $setStyleName(this$static, 'gwt-HTML');
  return this$static;
}

function $HTML_0(this$static, html){
  $HTML(this$static);
  $setHTML_0(this$static, html);
  return this$static;
}

function $HTML_1(this$static, html, wordWrap){
  $HTML_0(this$static, html);
  $setWordWrap(this$static, wordWrap);
  return this$static;
}

function $getHTML(this$static){
  return getInnerHTML(this$static.getElement());
}

function $setHTML_0(this$static, html){
  setInnerHTML(this$static.getElement(), html);
}

function HTML(){
}

_ = HTML.prototype = new Label();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTML';
_.typeId$ = 97;
function $$init_11(this$static){
  {
    $findNext(this$static);
  }
}

function $HTMLTable$1(this$static, this$1){
  this$static.this$1 = this$1;
  $$init_11(this$static);
  return this$static;
}

function $findNext(this$static){
  while (++this$static.nextIndex < this$static.this$1.widgetList.size) {
    if ($get_0(this$static.this$1.widgetList, this$static.nextIndex) !== null) {
      return;
    }
  }
}

function $hasNext_0(this$static){
  return this$static.nextIndex < this$static.this$1.widgetList.size;
}

function hasNext_0(){
  return $hasNext_0(this);
}

function next_1(){
  var result;
  if (!$hasNext_0(this)) {
    throw new NoSuchElementException();
  }
  result = $get_0(this.this$1.widgetList, this.nextIndex);
  $findNext(this);
  return result;
}

function HTMLTable$1(){
}

_ = HTMLTable$1.prototype = new Object_0();
_.hasNext = hasNext_0;
_.next_0 = next_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$1';
_.typeId$ = 98;
_.nextIndex = (-1);
function $HTMLTable$ColumnFormatter(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $prepareColumnGroup(this$static){
  if (this$static.columnGroup === null) {
    this$static.columnGroup = createElement('colgroup');
    insertChild(this$static.this$0.tableElem, this$static.columnGroup, 0);
    appendChild(this$static.columnGroup, createElement('col'));
  }
}

function HTMLTable$ColumnFormatter(){
}

_ = HTMLTable$ColumnFormatter.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$ColumnFormatter';
_.typeId$ = 99;
_.columnGroup = null;
function $getRow(this$static, elem, row){
  return elem.rows[row];
}

function HTMLTable$RowFormatter(){
}

_ = HTMLTable$RowFormatter.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$RowFormatter';
_.typeId$ = 100;
function $$init_12(this$static){
  this$static.widgetList = $ArrayList(new ArrayList());
}

function $HTMLTable$WidgetMapper(this$static){
  $$init_12(this$static);
  return this$static;
}

function $getWidget_0(this$static, elem){
  var index;
  index = getWidgetIndex(elem);
  if (index < 0) {
    return null;
  }
  return dynamicCast($get_0(this$static.widgetList, index), 15);
}

function $putWidget(this$static, widget){
  var index;
  if (this$static.freeList === null) {
    index = this$static.widgetList.size;
    $add_9(this$static.widgetList, widget);
  }
   else {
    index = this$static.freeList.index_0;
    $set(this$static.widgetList, index, widget);
    this$static.freeList = this$static.freeList.next;
  }
  setWidgetIndex(widget.getElement(), index);
}

function $removeImpl(this$static, elem, index){
  clearWidgetIndex(elem);
  $set(this$static.widgetList, index, null);
  this$static.freeList = $HTMLTable$WidgetMapper$FreeNode(new HTMLTable$WidgetMapper$FreeNode(), index, this$static.freeList);
}

function $removeWidgetByElement(this$static, elem){
  var index;
  index = getWidgetIndex(elem);
  $removeImpl(this$static, elem, index);
}

function $widgetIterator(this$static){
  return $HTMLTable$1(new HTMLTable$1(), this$static);
}

function clearWidgetIndex(elem){
  elem['__widgetID'] = null;
}

function getWidgetIndex(elem){
  var index = elem['__widgetID'];
  return index == null?-1:index;
}

function setWidgetIndex(elem, index){
  elem['__widgetID'] = index;
}

function HTMLTable$WidgetMapper(){
}

_ = HTMLTable$WidgetMapper.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$WidgetMapper';
_.typeId$ = 101;
_.freeList = null;
function $HTMLTable$WidgetMapper$FreeNode(this$static, index, next){
  this$static.index_0 = index;
  this$static.next = next;
  return this$static;
}

function HTMLTable$WidgetMapper$FreeNode(){
}

_ = HTMLTable$WidgetMapper$FreeNode.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$WidgetMapper$FreeNode';
_.typeId$ = 102;
_.index_0 = 0;
_.next = null;
function $clinit_108(){
  $clinit_108 = nullMethod;
  ALIGN_CENTER = $HasHorizontalAlignment$HorizontalAlignmentConstant(new HasHorizontalAlignment$HorizontalAlignmentConstant(), 'center');
  ALIGN_LEFT = $HasHorizontalAlignment$HorizontalAlignmentConstant(new HasHorizontalAlignment$HorizontalAlignmentConstant(), 'left');
  ALIGN_RIGHT = $HasHorizontalAlignment$HorizontalAlignmentConstant(new HasHorizontalAlignment$HorizontalAlignmentConstant(), 'right');
}

var ALIGN_CENTER, ALIGN_LEFT, ALIGN_RIGHT;
function $HasHorizontalAlignment$HorizontalAlignmentConstant(this$static, textAlignString){
  this$static.textAlignString = textAlignString;
  return this$static;
}

function HasHorizontalAlignment$HorizontalAlignmentConstant(){
}

_ = HasHorizontalAlignment$HorizontalAlignmentConstant.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HasHorizontalAlignment$HorizontalAlignmentConstant';
_.typeId$ = 103;
_.textAlignString = null;
function $clinit_112(){
  $clinit_112 = nullMethod;
  ALIGN_BOTTOM = $HasVerticalAlignment$VerticalAlignmentConstant(new HasVerticalAlignment$VerticalAlignmentConstant(), 'bottom');
  ALIGN_MIDDLE = $HasVerticalAlignment$VerticalAlignmentConstant(new HasVerticalAlignment$VerticalAlignmentConstant(), 'middle');
  ALIGN_TOP = $HasVerticalAlignment$VerticalAlignmentConstant(new HasVerticalAlignment$VerticalAlignmentConstant(), 'top');
}

var ALIGN_BOTTOM, ALIGN_MIDDLE, ALIGN_TOP;
function $HasVerticalAlignment$VerticalAlignmentConstant(this$static, verticalAlignString){
  this$static.verticalAlignString = verticalAlignString;
  return this$static;
}

function HasVerticalAlignment$VerticalAlignmentConstant(){
}

_ = HasVerticalAlignment$VerticalAlignmentConstant.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HasVerticalAlignment$VerticalAlignmentConstant';
_.typeId$ = 104;
_.verticalAlignString = null;
function $$init_14(this$static){
  this$static.horzAlign = ($clinit_108() , ALIGN_LEFT);
  this$static.vertAlign = ($clinit_112() , ALIGN_TOP);
}

function $HorizontalPanel(this$static){
  $CellPanel(this$static);
  $$init_14(this$static);
  this$static.tableRow = createTR();
  appendChild(this$static.body_0, this$static.tableRow);
  setElementProperty(this$static.table, 'cellSpacing', '0');
  setElementProperty(this$static.table, 'cellPadding', '0');
  return this$static;
}

function $add_3(this$static, w){
  var td;
  td = $createAlignedTd(this$static);
  appendChild(this$static.tableRow, td);
  $add_0(this$static, w, td);
}

function $createAlignedTd(this$static){
  var td;
  td = createTD();
  $setCellHorizontalAlignment(this$static, td, this$static.horzAlign);
  $setCellVerticalAlignment(this$static, td, this$static.vertAlign);
  return td;
}

function $insert_1(this$static, w, beforeIndex){
  var td;
  $checkIndexBoundsForInsertion(this$static, beforeIndex);
  td = $createAlignedTd(this$static);
  insertChild(this$static.tableRow, td, beforeIndex);
  $insert(this$static, w, td, beforeIndex, false);
}

function $remove_4(this$static, w){
  var removed, td;
  td = getParent(w.getElement());
  removed = $remove_1(this$static, w);
  if (removed) {
    removeChild(this$static.tableRow, td);
  }
  return removed;
}

function $setVerticalAlignment_0(this$static, align){
  this$static.vertAlign = align;
}

function remove_7(w){
  return $remove_4(this, w);
}

function HorizontalPanel(){
}

_ = HorizontalPanel.prototype = new CellPanel();
_.remove_0 = remove_7;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HorizontalPanel';
_.typeId$ = 105;
_.tableRow = null;
function $$init_22(this$static){
  this$static.widgets = initDims_0('[Lcom.google.gwt.user.client.ui.Widget;', [204], [15], [2], null);
  this$static.elements = initDims_0('[Lcom.google.gwt.user.client.Element;', [208], [8], [2], null);
}

function $SplitPanel(this$static, mainElem, splitElem, headElem, tailElem){
  $$init_22(this$static);
  this$static.setElement(mainElem);
  this$static.splitElem = splitElem;
  this$static.elements[0] = wrapJSO(headElem, Element);
  this$static.elements[1] = wrapJSO(tailElem, Element);
  $sinkEvents_0(this$static, 124);
  return this$static;
}

function $getElement(this$static, index){
  return this$static.elements[index];
}

function $setWidget_3(this$static, index, w){
  var oldWidget;
  oldWidget = this$static.widgets[index];
  if (oldWidget === w) {
    return;
  }
  if (w !== null) {
    $removeFromParent(w);
  }
  if (oldWidget !== null) {
    $orphan(this$static, oldWidget);
    removeChild(this$static.elements[index], oldWidget.getElement());
  }
  setCheck(this$static.widgets, index, w);
  if (w !== null) {
    appendChild(this$static.elements[index], w.getElement());
    $adopt(this$static, w);
  }
}

function $startResizingFrom(this$static, x, y){
  this$static.isResizing = true;
  this$static.onSplitterResizeStarted(x, y);
}

function $stopResizing(this$static){
  this$static.isResizing = false;
}

function addAbsolutePositoning(elem){
  setStyleAttribute(elem, 'position', 'absolute');
}

function addScrolling(elem){
  setStyleAttribute(elem, 'overflow', 'auto');
}

function expandToFitParentUsingCssOffsets(elem){
  var zeroSize;
  zeroSize = '0px';
  addAbsolutePositoning(elem);
  setLeft(elem, '0px');
  setRight(elem, '0px');
  setTop(elem, '0px');
  setBottom(elem, '0px');
}

function getOffsetWidth_0(elem){
  return getElementPropertyInt(elem, 'offsetWidth');
}

function iterator_4(){
  return createWidgetIterator(this, this.widgets);
}

function onBrowserEvent_11(event_0){
  var target;
  switch (eventGetType(event_0)) {
    case 4:
      {
        target = eventGetTarget(event_0);
        if (isOrHasChild(this.splitElem, target)) {
          $startResizingFrom(this, eventGetClientX(event_0) - $getAbsoluteLeft_0(this), eventGetClientY(event_0) - $getAbsoluteTop_0(this));
          setCapture(this.getElement());
          eventPreventDefault(event_0);
        }
        break;
      }

    case 8:
      {
        releaseCapture(this.getElement());
        $stopResizing(this);
        break;
      }

    case 64:
      {
        if (this.isResizing) {
          this.onSplitterResize(eventGetClientX(event_0) - $getAbsoluteLeft_0(this), eventGetClientY(event_0) - $getAbsoluteTop_0(this));
          eventPreventDefault(event_0);
        }
        break;
      }

  }
}

function preventBoxStyles(elem){
  setIntStyleAttribute(elem, 'padding', 0);
  setIntStyleAttribute(elem, 'margin', 0);
  setStyleAttribute(elem, 'border', 'none');
  return elem;
}

function remove_9(widget){
  if (this.widgets[0] === widget) {
    $setWidget_3(this, 0, null);
    return true;
  }
   else if (this.widgets[1] === widget) {
    $setWidget_3(this, 1, null);
    return true;
  }
  return false;
}

function setBottom(elem, size){
  setStyleAttribute(elem, 'bottom', size);
}

function setHeight_0(elem, height){
  setStyleAttribute(elem, 'height', height);
}

function setLeft(elem, left){
  setStyleAttribute(elem, 'left', left);
}

function setRight(elem, right){
  setStyleAttribute(elem, 'right', right);
}

function setTop(elem, top){
  setStyleAttribute(elem, 'top', top);
}

function setWidth_1(elem, width){
  setStyleAttribute(elem, 'width', width);
}

function SplitPanel(){
}

_ = SplitPanel.prototype = new Panel();
_.iterator_0 = iterator_4;
_.onBrowserEvent = onBrowserEvent_11;
_.remove_0 = remove_9;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SplitPanel';
_.typeId$ = 106;
_.isResizing = false;
_.splitElem = null;
function $$init_15(this$static){
  this$static.impl = new HorizontalSplitPanel$ImplSafari();
}

function $HorizontalSplitPanel(this$static){
  $HorizontalSplitPanel_0(this$static, $HorizontalSplitPanelImages_generatedBundle(new HorizontalSplitPanelImages_generatedBundle()));
  return this$static;
}

function $HorizontalSplitPanel_0(this$static, images){
  $SplitPanel(this$static, createDiv(), createDiv(), preventBoxStyles(createDiv()), preventBoxStyles(createDiv()));
  $$init_15(this$static);
  this$static.container = preventBoxStyles(createDiv());
  $buildDOM(this$static, ($clinit_120() , horizontalSplitPanelThumb_SINGLETON));
  $setStyleName(this$static, 'gwt-HorizontalSplitPanel');
  $init_3(this$static.impl, this$static);
  this$static.setHeight('100%');
  return this$static;
}

function $buildDOM(this$static, thumbImage){
  var leftDiv, rightDiv, splitDiv;
  leftDiv = $getElement(this$static, 0);
  rightDiv = $getElement(this$static, 1);
  splitDiv = this$static.splitElem;
  appendChild(this$static.getElement(), this$static.container);
  appendChild(this$static.container, leftDiv);
  appendChild(this$static.container, splitDiv);
  appendChild(this$static.container, rightDiv);
  setInnerHTML(splitDiv, "<table class='hsplitter' height='100%' cellpadding='0' cellspacing='0'><tr><td align='center' valign='middle'>" + $getHTML_1(thumbImage));
  addScrolling(leftDiv);
  addScrolling(rightDiv);
}

function $setLeftWidget(this$static, w){
  $setWidget_3(this$static, 0, w);
}

function $setRightWidget(this$static, w){
  $setWidget_3(this$static, 1, w);
}

function $setSplitPosition_0(this$static, pos){
  var leftElem;
  this$static.lastSplitPosition = pos;
  leftElem = $getElement(this$static, 0);
  setWidth_1(leftElem, pos);
  $setSplitPosition(this$static.impl, getOffsetWidth_0(leftElem));
}

function onLoad_0(){
  $setSplitPosition_0(this, this.lastSplitPosition);
  addCommand($HorizontalSplitPanel$2(new HorizontalSplitPanel$2(), this));
}

function onSplitterResize(x, y){
  $onSplitResize(this.impl, this.initialLeftWidth + x - this.initialThumbPos);
}

function onSplitterResizeStarted(x, y){
  this.initialThumbPos = x;
  this.initialLeftWidth = getOffsetWidth_0($getElement(this, 0));
}

function onUnload_0(){
}

function HorizontalSplitPanel(){
}

_ = HorizontalSplitPanel.prototype = new SplitPanel();
_.onLoad = onLoad_0;
_.onSplitterResize = onSplitterResize;
_.onSplitterResizeStarted = onSplitterResizeStarted;
_.onUnload = onUnload_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HorizontalSplitPanel';
_.typeId$ = 107;
_.container = null;
_.initialLeftWidth = 0;
_.initialThumbPos = 0;
_.lastSplitPosition = '50%';
function $HorizontalSplitPanel$2(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function execute_0(){
  $setSplitPosition_0(this.this$0, this.this$0.lastSplitPosition);
}

function HorizontalSplitPanel$2(){
}

_ = HorizontalSplitPanel$2.prototype = new Object_0();
_.execute = execute_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HorizontalSplitPanel$2';
_.typeId$ = 108;
function $init_4(this$static, panel){
  var rightElem;
  this$static.panel = panel;
  setStyleAttribute(panel.getElement(), 'position', 'relative');
  rightElem = $getElement(panel, 1);
  expandToFitParentHorizontally($getElement(panel, 0));
  expandToFitParentHorizontally(rightElem);
  expandToFitParentHorizontally(panel.splitElem);
  expandToFitParentUsingCssOffsets(panel.container);
  setRight(rightElem, '0px');
}

function $onSplitResize(this$static, px){
  $setSplitPosition(this$static, px);
}

function $setSplitPosition(this$static, px){
  var newRightWidth, rightElem, rootElemWidth, splitElem, splitElemWidth;
  splitElem = this$static.panel.splitElem;
  rootElemWidth = getOffsetWidth_0(this$static.panel.container);
  splitElemWidth = getOffsetWidth_0(splitElem);
  if (rootElemWidth < splitElemWidth) {
    return;
  }
  newRightWidth = rootElemWidth - px - splitElemWidth;
  if (px < 0) {
    px = 0;
    newRightWidth = rootElemWidth - splitElemWidth;
  }
   else if (newRightWidth < 0) {
    px = rootElemWidth - splitElemWidth;
    newRightWidth = 0;
  }
  rightElem = $getElement(this$static.panel, 1);
  setWidth_1($getElement(this$static.panel, 0), px + 'px');
  setLeft(splitElem, px + 'px');
  setLeft(rightElem, px + splitElemWidth + 'px');
}

function expandToFitParentHorizontally(elem){
  var zeroSize;
  addAbsolutePositoning(elem);
  zeroSize = '0px';
  setTop(elem, '0px');
  setBottom(elem, '0px');
}

function HorizontalSplitPanel$Impl(){
}

_ = HorizontalSplitPanel$Impl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HorizontalSplitPanel$Impl';
_.typeId$ = 109;
_.panel = null;
function $init_3(this$static, panel){
  var fullSize;
  this$static.panel = panel;
  fullSize = '100%';
  $init_4(this$static, panel);
  setHeight_0(panel.container, '100%');
  setHeight_0($getElement(panel, 0), '100%');
  setHeight_0($getElement(panel, 1), '100%');
  setHeight_0(panel.splitElem, '100%');
}

function HorizontalSplitPanel$ImplSafari(){
}

_ = HorizontalSplitPanel$ImplSafari.prototype = new HorizontalSplitPanel$Impl();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HorizontalSplitPanel$ImplSafari';
_.typeId$ = 110;
function $clinit_120(){
  $clinit_120 = nullMethod;
  IMAGE_BUNDLE_URL_2 = getModuleBaseURL() + '4BF90CCB5E6B04D22EF1776E8EBF09F8.cache.png';
  horizontalSplitPanelThumb_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL_2, 0, 0, 7, 7);
}

function $HorizontalSplitPanelImages_generatedBundle(this$static){
  $clinit_120();
  return this$static;
}

function HorizontalSplitPanelImages_generatedBundle(){
}

_ = HorizontalSplitPanelImages_generatedBundle.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HorizontalSplitPanelImages_generatedBundle';
_.typeId$ = 111;
var IMAGE_BUNDLE_URL_2, horizontalSplitPanelThumb_SINGLETON;
function $clinit_128(){
  $clinit_128 = nullMethod;
  $HashMap(new HashMap());
}

function $Image(this$static){
  $clinit_128();
  $changeState(this$static, $Image$UnclippedState(new Image$UnclippedState(), this$static));
  $setStyleName(this$static, 'gwt-Image');
  return this$static;
}

function $Image_0(this$static, url){
  $clinit_128();
  $changeState(this$static, $Image$UnclippedState_0(new Image$UnclippedState(), this$static, url));
  $setStyleName(this$static, 'gwt-Image');
  return this$static;
}

function $Image_1(this$static, url, left, top, width, height){
  $clinit_128();
  $changeState(this$static, $Image$ClippedState(new Image$ClippedState(), this$static, url, left, top, width, height));
  $setStyleName(this$static, 'gwt-Image');
  return this$static;
}

function $changeState(this$static, newState){
  this$static.state = newState;
}

function $getWidth(this$static){
  return this$static.state.getWidth(this$static);
}

function $setUrlAndVisibleRect(this$static, url, left, top, width, height){
  this$static.state.setUrlAndVisibleRect(this$static, url, left, top, width, height);
}

function onBrowserEvent_5(event_0){
  switch (eventGetType(event_0)) {
    case 1:
      {
        break;
      }

    case 4:
    case 8:
    case 64:
    case 16:
    case 32:
      {
        break;
      }

    case 131072:
      break;
    case 32768:
      {
        break;
      }

    case 65536:
      {
        break;
      }

  }
}

function Image_0(){
}

_ = Image_0.prototype = new Widget();
_.onBrowserEvent = onBrowserEvent_5;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image';
_.typeId$ = 112;
_.state = null;
function execute_1(){
}

function Image$1(){
}

_ = Image$1.prototype = new Object_0();
_.execute = execute_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image$1';
_.typeId$ = 113;
function Image$State(){
}

_ = Image$State.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image$State';
_.typeId$ = 114;
function $clinit_124(){
  $clinit_124 = nullMethod;
  impl_2 = new ClippedImageImpl();
}

function $Image$ClippedState(this$static, image, url, left, top, width, height){
  $clinit_124();
  this$static.left = left;
  this$static.top = top;
  this$static.width_0 = width;
  this$static.height_0 = height;
  this$static.url = url;
  image.setElement($createStructure(impl_2, url, left, top, width, height));
  $sinkEvents_0(image, 131197);
  $fireSyntheticLoadEvent(this$static, image);
  return this$static;
}

function $fireSyntheticLoadEvent(this$static, image){
  addCommand(new Image$1());
}

function getWidth(image){
  return this.width_0;
}

function setUrlAndVisibleRect(image, url, left, top, width, height){
  if (!$equals_1(this.url, url) || this.left != left || this.top != top || this.width_0 != width || this.height_0 != height) {
    this.url = url;
    this.left = left;
    this.top = top;
    this.width_0 = width;
    this.height_0 = height;
    $adjust(impl_2, image.getElement(), url, left, top, width, height);
    $fireSyntheticLoadEvent(this, image);
  }
}

function Image$ClippedState(){
}

_ = Image$ClippedState.prototype = new Image$State();
_.getWidth = getWidth;
_.setUrlAndVisibleRect = setUrlAndVisibleRect;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image$ClippedState';
_.typeId$ = 115;
_.height_0 = 0;
_.left = 0;
_.top = 0;
_.url = null;
_.width_0 = 0;
var impl_2;
function $Image$UnclippedState(this$static, image){
  image.setElement(createImg());
  $sinkEvents_0(image, 229501);
  return this$static;
}

function $Image$UnclippedState_0(this$static, image, url){
  $Image$UnclippedState(this$static, image);
  $setUrl(this$static, image, url);
  return this$static;
}

function $setUrl(this$static, image, url){
  setImgSrc(image.getElement(), url);
}

function getWidth_0(image){
  return getElementPropertyInt(image.getElement(), 'width');
}

function setUrlAndVisibleRect_0(image, url, left, top, width, height){
  $changeState(image, $Image$ClippedState(new Image$ClippedState(), image, url, left, top, width, height));
}

function Image$UnclippedState(){
}

_ = Image$UnclippedState.prototype = new Image$State();
_.getWidth = getWidth_0;
_.setUrlAndVisibleRect = setUrlAndVisibleRect_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image$UnclippedState';
_.typeId$ = 116;
function $KeyboardListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireKeyDown(this$static, sender, keyCode, modifiers){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 16);
    listener.onKeyDown(sender, keyCode, modifiers);
  }
}

function $fireKeyPress(this$static, sender, key_0, modifiers){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 16);
    listener.onKeyPress(sender, key_0, modifiers);
  }
}

function $fireKeyUp(this$static, sender, keyCode, modifiers){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 16);
    listener.onKeyUp(sender, keyCode, modifiers);
  }
}

function $fireKeyboardEvent(this$static, sender, event_0){
  var modifiers;
  modifiers = getKeyboardModifiers(event_0);
  switch (eventGetType(event_0)) {
    case 128:
      $fireKeyDown(this$static, sender, narrow_char(eventGetKeyCode(event_0)), modifiers);
      break;
    case 512:
      $fireKeyUp(this$static, sender, narrow_char(eventGetKeyCode(event_0)), modifiers);
      break;
    case 256:
      $fireKeyPress(this$static, sender, narrow_char(eventGetKeyCode(event_0)), modifiers);
      break;
  }
}

function getKeyboardModifiers(event_0){
  return (eventGetShiftKey(event_0)?1:0) | (eventGetMetaKey(event_0)?8:0) | (eventGetCtrlKey(event_0)?2:0) | (eventGetAltKey(event_0)?4:0);
}

function KeyboardListenerCollection(){
}

_ = KeyboardListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'KeyboardListenerCollection';
_.typeId$ = 117;
function $clinit_136(){
  $clinit_136 = nullMethod;
  $clinit_217() , implWidget;
  impl_3 = new ListBox$ImplSafari();
}

function $ListBox(this$static){
  $clinit_136();
  $ListBox_0(this$static, false);
  return this$static;
}

function $ListBox_0(this$static, isMultipleSelect){
  $clinit_136();
  $FocusWidget_0(this$static, createSelect(isMultipleSelect));
  $sinkEvents_0(this$static, 1024);
  $setStyleName(this$static, 'gwt-ListBox');
  return this$static;
}

function $addChangeListener(this$static, listener){
  if (this$static.changeListeners === null) {
    this$static.changeListeners = $ChangeListenerCollection(new ChangeListenerCollection());
  }
  $add_9(this$static.changeListeners, listener);
}

function $addItem(this$static, item){
  $insertItem(this$static, item, (-1));
}

function $addItem_0(this$static, item, value){
  $insertItem_0(this$static, item, value, (-1));
}

function $checkIndex(this$static, index){
  if (index < 0 || index >= $getItemCount_0(this$static)) {
    throw new IndexOutOfBoundsException();
  }
}

function $clear_0(this$static){
  $clear(impl_3, this$static.getElement());
}

function $getItemCount_0(this$static){
  return $getItemCount(impl_3, this$static.getElement());
}

function $getSelectedIndex(this$static){
  return getElementPropertyInt(this$static.getElement(), 'selectedIndex');
}

function $getValue(this$static, index){
  $checkIndex(this$static, index);
  return $getItemValue(impl_3, this$static.getElement(), index);
}

function $insertItem(this$static, item, index){
  $insertItem_0(this$static, item, item, index);
}

function $insertItem_0(this$static, item, value, index){
  insertListItem(this$static.getElement(), item, value, index);
}

function $setMultipleSelect(this$static, multiple){
  setElementPropertyBoolean(this$static.getElement(), 'multiple', multiple);
}

function $setSelectedIndex(this$static, index){
  setElementPropertyInt(this$static.getElement(), 'selectedIndex', index);
}

function $setVisibleItemCount(this$static, visibleItems){
  setElementPropertyInt(this$static.getElement(), 'size', visibleItems);
}

function onBrowserEvent_7(event_0){
  if (eventGetType(event_0) == 1024) {
    if (this.changeListeners !== null) {
      $fireChange(this.changeListeners, this);
    }
  }
   else {
    $onBrowserEvent(this, event_0);
  }
}

function ListBox(){
}

_ = ListBox.prototype = new FocusWidget();
_.onBrowserEvent = onBrowserEvent_7;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ListBox';
_.typeId$ = 118;
_.changeListeners = null;
var impl_3;
function ListBox$Impl(){
}

_ = ListBox$Impl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ListBox$Impl';
_.typeId$ = 119;
function $clear(this$static, select){
  select.innerText = '';
}

function $getItemCount(this$static, select){
  return select.children.length;
}

function $getItemValue(this$static, select, index){
  return select.children[index].value;
}

function ListBox$ImplSafari(){
}

_ = ListBox$ImplSafari.prototype = new ListBox$Impl();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ListBox$ImplSafari';
_.typeId$ = 120;
function $$init_17(this$static){
  this$static.items = $ArrayList(new ArrayList());
}

function $MenuBar(this$static){
  $MenuBar_0(this$static, false);
  return this$static;
}

function $MenuBar_0(this$static, vertical){
  var outer, table, tr;
  $$init_17(this$static);
  table = createTable();
  this$static.body_0 = createTBody();
  appendChild(table, this$static.body_0);
  if (!vertical) {
    tr = createTR();
    appendChild(this$static.body_0, tr);
  }
  this$static.vertical = vertical;
  outer = createDiv();
  appendChild(outer, table);
  this$static.setElement(outer);
  $sinkEvents_0(this$static, 49);
  $setStyleName(this$static, 'gwt-MenuBar');
  return this$static;
}

function $addItem_1(this$static, item){
  var tr;
  if (this$static.vertical) {
    tr = createTR();
    appendChild(this$static.body_0, tr);
  }
   else {
    tr = getChild(this$static.body_0, 0);
  }
  appendChild(tr, item.getElement());
  $setParentMenu(item, this$static);
  $setSelectionStyle(item, false);
  $add_9(this$static.items, item);
}

function $addItem_3(this$static, text, asHTML, cmd){
  var item;
  item = $MenuItem_2(new MenuItem(), text, asHTML, cmd);
  $addItem_1(this$static, item);
  return item;
}

function $addItem_4(this$static, text, asHTML, popup){
  var item;
  item = $MenuItem_3(new MenuItem(), text, asHTML, popup);
  $addItem_1(this$static, item);
  return item;
}

function $addItem_2(this$static, text, cmd){
  var item;
  item = $MenuItem(new MenuItem(), text, cmd);
  $addItem_1(this$static, item);
  return item;
}

function $clearItems(this$static){
  var container;
  container = $getItemContainerElement(this$static);
  while (getChildCount(container) > 0) {
    removeChild(container, getChild(container, 0));
  }
  $clear_2(this$static.items);
}

function $close(this$static){
  if (this$static.parentMenu !== null) {
    $hide(this$static.parentMenu.popup);
  }
}

function $closeAllParents(this$static){
  var curMenu;
  curMenu = this$static;
  while (curMenu !== null) {
    $close(curMenu);
    if (curMenu.parentMenu === null && curMenu.selectedItem !== null) {
      $setSelectionStyle(curMenu.selectedItem, false);
      curMenu.selectedItem = null;
    }
    curMenu = curMenu.parentMenu;
  }
}

function $doItemAction(this$static, item, fireCommand){
  var cmd;
  if (this$static.shownChildMenu !== null && item.subMenu === this$static.shownChildMenu) {
    return;
  }
  if (this$static.shownChildMenu !== null) {
    $onHide(this$static.shownChildMenu);
    $hide(this$static.popup);
  }
  if (item.subMenu === null) {
    if (fireCommand) {
      $closeAllParents(this$static);
      cmd = item.command;
      if (cmd !== null) {
        addCommand(cmd);
      }
    }
    return;
  }
  $selectItem(this$static, item);
  this$static.popup = $MenuBar$1(new MenuBar$1(), true, this$static, item);
  $addPopupListener(this$static.popup, this$static);
  if (this$static.vertical) {
    $setPopupPosition(this$static.popup, $getAbsoluteLeft_0(item) + item.getOffsetWidth(), $getAbsoluteTop_0(item));
  }
   else {
    $setPopupPosition(this$static.popup, $getAbsoluteLeft_0(item), $getAbsoluteTop_0(item) + item.getOffsetHeight());
  }
  this$static.shownChildMenu = item.subMenu;
  item.subMenu.parentMenu = this$static;
  $show_0(this$static.popup);
}

function $findItem(this$static, hItem){
  var i, item;
  for (i = 0; i < this$static.items.size; ++i) {
    item = dynamicCast($get_0(this$static.items, i), 17);
    if (isOrHasChild(item.getElement(), hItem)) {
      return item;
    }
  }
  return null;
}

function $getItemContainerElement(this$static){
  if (this$static.vertical) {
    return this$static.body_0;
  }
   else {
    return getChild(this$static.body_0, 0);
  }
}

function $itemOver(this$static, item){
  if (item === null) {
    if (this$static.selectedItem !== null && this$static.shownChildMenu === this$static.selectedItem.subMenu) {
      return;
    }
  }
  $selectItem(this$static, item);
  if (item !== null) {
    if (this$static.shownChildMenu !== null || this$static.parentMenu !== null || this$static.autoOpen) {
      $doItemAction(this$static, item, false);
    }
  }
}

function $onHide(this$static){
  if (this$static.shownChildMenu !== null) {
    $onHide(this$static.shownChildMenu);
    $hide(this$static.popup);
  }
}

function $onShow(this$static){
  if (this$static.items.size > 0) {
    $selectItem(this$static, dynamicCast($get_0(this$static.items, 0), 17));
  }
}

function $selectItem(this$static, item){
  if (item === this$static.selectedItem) {
    return;
  }
  if (this$static.selectedItem !== null) {
    $setSelectionStyle(this$static.selectedItem, false);
  }
  if (item !== null) {
    $setSelectionStyle(item, true);
  }
  this$static.selectedItem = item;
}

function $setAutoOpen(this$static, autoOpen){
  this$static.autoOpen = autoOpen;
}

function onBrowserEvent_8(event_0){
  var item;
  item = $findItem(this, eventGetTarget(event_0));
  switch (eventGetType(event_0)) {
    case 1:
      {
        if (item !== null) {
          $doItemAction(this, item, true);
        }
        break;
      }

    case 16:
      {
        if (item !== null) {
          $itemOver(this, item);
        }
        break;
      }

    case 32:
      {
        if (item !== null) {
          $itemOver(this, null);
        }
        break;
      }

  }
}

function onDetach_1(){
  if (this.popup !== null) {
    $hide(this.popup);
  }
  $onDetach(this);
}

function onPopupClosed(sender, autoClosed){
  if (autoClosed) {
    $closeAllParents(this);
  }
  $onHide(this);
  this.shownChildMenu = null;
  this.popup = null;
}

function MenuBar(){
}

_ = MenuBar.prototype = new Widget();
_.onBrowserEvent = onBrowserEvent_8;
_.onDetach = onDetach_1;
_.onPopupClosed = onPopupClosed;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'MenuBar';
_.typeId$ = 121;
_.autoOpen = false;
_.body_0 = null;
_.parentMenu = null;
_.popup = null;
_.selectedItem = null;
_.shownChildMenu = null;
_.vertical = false;
function $clinit_137(){
  $clinit_137 = nullMethod;
  $clinit_149();
}

function $$init_16(this$static){
  {
    this$static.setWidget(this$static.val$item.subMenu);
    $onShow(this$static.val$item.subMenu);
  }
}

function $MenuBar$1(this$static, $anonymous0, this$0, val$item){
  $clinit_137();
  this$static.val$item = val$item;
  $PopupPanel_0(this$static, $anonymous0);
  $$init_16(this$static);
  return this$static;
}

function onEventPreview_0(event_0){
  var parentMenuElement, target;
  switch (eventGetType(event_0)) {
    case 1:
      target = eventGetTarget(event_0);
      parentMenuElement = this.val$item.parentMenu.getElement();
      if (isOrHasChild(parentMenuElement, target)) {
        return false;
      }

      break;
  }
  return $onEventPreview(this, event_0);
}

function MenuBar$1(){
}

_ = MenuBar$1.prototype = new PopupPanel();
_.onEventPreview = onEventPreview_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'MenuBar$1';
_.typeId$ = 122;
function $MenuItem(this$static, text, cmd){
  $MenuItem_1(this$static, text, false);
  $setCommand(this$static, cmd);
  return this$static;
}

function $MenuItem_2(this$static, text, asHTML, cmd){
  $MenuItem_1(this$static, text, asHTML);
  $setCommand(this$static, cmd);
  return this$static;
}

function $MenuItem_0(this$static, text, subMenu){
  $MenuItem_1(this$static, text, false);
  $setSubMenu(this$static, subMenu);
  return this$static;
}

function $MenuItem_3(this$static, text, asHTML, subMenu){
  $MenuItem_1(this$static, text, asHTML);
  $setSubMenu(this$static, subMenu);
  return this$static;
}

function $MenuItem_1(this$static, text, asHTML){
  this$static.setElement(createTD());
  $setSelectionStyle(this$static, false);
  if (asHTML) {
    $setHTML_1(this$static, text);
  }
   else {
    $setText_4(this$static, text);
  }
  $setStyleName(this$static, 'gwt-MenuItem');
  return this$static;
}

function $setCommand(this$static, cmd){
  this$static.command = cmd;
}

function $setHTML_1(this$static, html){
  setInnerHTML(this$static.getElement(), html);
}

function $setParentMenu(this$static, parentMenu){
  this$static.parentMenu = parentMenu;
}

function $setSelectionStyle(this$static, selected){
  if (selected) {
    $addStyleDependentName(this$static, 'selected');
  }
   else {
    $removeStyleDependentName(this$static, 'selected');
  }
}

function $setSubMenu(this$static, subMenu){
  this$static.subMenu = subMenu;
}

function $setText_4(this$static, text){
  setInnerText(this$static.getElement(), text);
}

function MenuItem(){
}

_ = MenuItem.prototype = new UIObject();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'MenuItem';
_.typeId$ = 123;
_.command = null;
_.parentMenu = null;
_.subMenu = null;
function $MouseListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireMouseDown(this$static, sender, x, y){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 18);
    listener.onMouseDown(sender, x, y);
  }
}

function $fireMouseEnter(this$static, sender){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 18);
    listener.onMouseEnter(sender);
  }
}

function $fireMouseEvent(this$static, sender, event_0){
  var from, senderElem, to, x, y;
  senderElem = sender.getElement();
  x = eventGetClientX(event_0) - getAbsoluteLeft(senderElem) + getElementPropertyInt(senderElem, 'scrollLeft') + getScrollLeft();
  y = eventGetClientY(event_0) - getAbsoluteTop(senderElem) + getElementPropertyInt(senderElem, 'scrollTop') + getScrollTop();
  switch (eventGetType(event_0)) {
    case 4:
      $fireMouseDown(this$static, sender, x, y);
      break;
    case 8:
      $fireMouseUp(this$static, sender, x, y);
      break;
    case 64:
      $fireMouseMove(this$static, sender, x, y);
      break;
    case 16:
      from = eventGetFromElement(event_0);
      if (!isOrHasChild(senderElem, from)) {
        $fireMouseEnter(this$static, sender);
      }

      break;
    case 32:
      to = eventGetToElement(event_0);
      if (!isOrHasChild(senderElem, to)) {
        $fireMouseLeave(this$static, sender);
      }

      break;
  }
}

function $fireMouseLeave(this$static, sender){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 18);
    listener.onMouseLeave(sender);
  }
}

function $fireMouseMove(this$static, sender, x, y){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 18);
    listener.onMouseMove(sender, x, y);
  }
}

function $fireMouseUp(this$static, sender, x, y){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 18);
    listener.onMouseUp(sender, x, y);
  }
}

function MouseListenerCollection(){
}

_ = MouseListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'MouseListenerCollection';
_.typeId$ = 124;
function SuggestOracle(){
}

_ = SuggestOracle.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestOracle';
_.typeId$ = 125;
function $clinit_143(){
  $clinit_143 = nullMethod;
  convertMe = $HTML(new HTML());
}

function $$init_18(this$static){
  this$static.tree = $PrefixTree(new PrefixTree());
  this$static.toCandidates = $HashMap(new HashMap());
  this$static.toRealSuggestions = $HashMap(new HashMap());
}

function $MultiWordSuggestOracle(this$static){
  $clinit_143();
  $MultiWordSuggestOracle_0(this$static, ' ');
  return this$static;
}

function $MultiWordSuggestOracle_0(this$static, whitespaceChars){
  var i;
  $clinit_143();
  $$init_18(this$static);
  this$static.whitespaceChars = initDims_0('[C', [202], [(-1)], [$length(whitespaceChars)], 0);
  for (i = 0; i < $length(whitespaceChars); i++) {
    this$static.whitespaceChars[i] = $charAt(whitespaceChars, i);
  }
  return this$static;
}

function $add_4(this$static, suggestion){
  var candidate, i, l, word, words;
  candidate = $normalizeSuggestion(this$static, suggestion);
  $put(this$static.toRealSuggestions, candidate, suggestion);
  words = $split(candidate, ' ');
  for (i = 0; i < words.length_0; i++) {
    word = words[i];
    $add_5(this$static.tree, word);
    l = dynamicCast($get_1(this$static.toCandidates, word), 19);
    if (l === null) {
      l = $HashSet(new HashSet());
      $put(this$static.toCandidates, word, l);
    }
    $add_10(l, candidate);
  }
}

function $computeItemsFor(this$static, query, limit){
  var candidates;
  query = $normalizeSearch(this$static, query);
  candidates = $createCandidatesFromSearch(this$static, query, limit);
  return $convertToFormattedSuggestions(this$static, query, candidates);
}

function $convertToFormattedSuggestions(this$static, query, candidates){
  var accum, candidate, cursor, end, endIndex, formattedSuggestion, i, index, part1, part2, suggestion, suggestions;
  suggestions = $ArrayList(new ArrayList());
  for (i = 0; i < candidates.size; i++) {
    candidate = dynamicCast($get_0(candidates, i), 1);
    index = 0;
    cursor = 0;
    formattedSuggestion = dynamicCast($get_1(this$static.toRealSuggestions, candidate), 1);
    accum = $StringBuffer(new StringBuffer());
    while (true) {
      index = $indexOf_2(candidate, query, index);
      if (index == (-1)) {
        break;
      }
      endIndex = index + $length(query);
      if (index == 0 || 32 == $charAt(candidate, index - 1)) {
        part1 = $escapeText(this$static, $substring_0(formattedSuggestion, cursor, index));
        part2 = $escapeText(this$static, $substring_0(formattedSuggestion, index, endIndex));
        cursor = endIndex;
        $append($append($append($append(accum, part1), '<strong>'), part2), '<\/strong>');
      }
      index = endIndex;
    }
    if (cursor == 0) {
      continue;
    }
    end = $escapeText(this$static, $substring(formattedSuggestion, cursor));
    $append(accum, end);
    suggestion = $MultiWordSuggestOracle$MultiWordSuggestion(new MultiWordSuggestOracle$MultiWordSuggestion(), formattedSuggestion, $toString_0(accum));
    $add_9(suggestions, suggestion);
  }
  return suggestions;
}

function $createCandidatesFromSearch(this$static, query, limit){
  var candidateSet, candidates, i, searchWords, thisWordChoices, word;
  candidates = $ArrayList(new ArrayList());
  if ($length(query) == 0) {
    return candidates;
  }
  searchWords = $split(query, ' ');
  candidateSet = null;
  for (i = 0; i < searchWords.length_0; i++) {
    word = searchWords[i];
    if ($length(word) == 0 || $matches(word, ' ')) {
      continue;
    }
    thisWordChoices = $createCandidatesFromWord(this$static, word);
    if (candidateSet === null) {
      candidateSet = thisWordChoices;
    }
     else {
      $retainAll(candidateSet, thisWordChoices);
      if (candidateSet.map.size < 2) {
        break;
      }
    }
  }
  if (candidateSet !== null) {
    $addAll(candidates, candidateSet);
    sort_0(candidates);
    for (i = candidates.size - 1; i > limit; i--) {
      $remove_12(candidates, i);
    }
  }
  return candidates;
}

function $createCandidatesFromWord(this$static, query){
  var belongsTo, candidateSet, i, words;
  candidateSet = $HashSet(new HashSet());
  words = $getSuggestions(this$static.tree, query, 2147483647);
  if (words !== null) {
    for (i = 0; i < words.size; i++) {
      belongsTo = dynamicCast($get_1(this$static.toCandidates, $get_0(words, i)), 20);
      if (belongsTo !== null) {
        candidateSet.addAll(belongsTo);
      }
    }
  }
  return candidateSet;
}

function $escapeText(this$static, escapeMe){
  var escaped;
  $setText_3(convertMe, escapeMe);
  escaped = $getHTML(convertMe);
  return escaped;
}

function $normalizeSearch(this$static, search){
  search = $normalizeSuggestion(this$static, search);
  search = $replaceAll(search, '\\s+', ' ');
  return $trim(search);
}

function $normalizeSuggestion(this$static, formattedSuggestion){
  var i, ignore;
  formattedSuggestion = $toLowerCase(formattedSuggestion);
  if (this$static.whitespaceChars !== null) {
    for (i = 0; i < this$static.whitespaceChars.length_0; i++) {
      ignore = this$static.whitespaceChars[i];
      formattedSuggestion = $replace(formattedSuggestion, ignore, 32);
    }
  }
  return formattedSuggestion;
}

function $requestSuggestions(this$static, request, callback){
  var response, suggestions;
  suggestions = $computeItemsFor(this$static, request.query, request.limit);
  response = $SuggestOracle$Response(new SuggestOracle$Response(), suggestions);
  $onSuggestionsReady(callback, request, response);
}

function MultiWordSuggestOracle(){
}

_ = MultiWordSuggestOracle.prototype = new SuggestOracle();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'MultiWordSuggestOracle';
_.typeId$ = 126;
_.whitespaceChars = null;
var convertMe;
function $MultiWordSuggestOracle$MultiWordSuggestion(this$static, replacementString, displayString){
  this$static.replacementString = replacementString;
  this$static.displayString = displayString;
  return this$static;
}

function getDisplayString(){
  return this.displayString;
}

function getReplacementString(){
  return this.replacementString;
}

function MultiWordSuggestOracle$MultiWordSuggestion(){
}

_ = MultiWordSuggestOracle$MultiWordSuggestion.prototype = new Object_0();
_.getDisplayString = getDisplayString;
_.getReplacementString = getReplacementString;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'MultiWordSuggestOracle$MultiWordSuggestion';
_.typeId$ = 127;
_.displayString = null;
_.replacementString = null;
function $clinit_197(){
  $clinit_197 = nullMethod;
  $clinit_217() , implWidget;
  impl_5 = new TextBoxImpl();
}

function $TextBoxBase(this$static, elem){
  $clinit_197();
  $FocusWidget_0(this$static, elem);
  $sinkEvents_0(this$static, 1024);
  return this$static;
}

function $addClickListener_0(this$static, listener){
  if (this$static.clickListeners === null) {
    this$static.clickListeners = $ClickListenerCollection(new ClickListenerCollection());
  }
  $add_9(this$static.clickListeners, listener);
}

function $addKeyboardListener(this$static, listener){
  if (this$static.keyboardListeners === null) {
    this$static.keyboardListeners = $KeyboardListenerCollection(new KeyboardListenerCollection());
  }
  $add_9(this$static.keyboardListeners, listener);
}

function $getText(this$static){
  return getElementProperty(this$static.getElement(), 'value');
}

function $setReadOnly(this$static, readOnly){
  var readOnlyStyle;
  setElementPropertyBoolean(this$static.getElement(), 'readOnly', readOnly);
  readOnlyStyle = 'readonly';
  if (readOnly) {
    $addStyleDependentName(this$static, readOnlyStyle);
  }
   else {
    $removeStyleDependentName(this$static, readOnlyStyle);
  }
}

function $setText_5(this$static, text){
  setElementProperty(this$static.getElement(), 'value', text !== null?text:'');
}

function addClickListener_0(listener){
  $addClickListener_0(this, listener);
}

function addKeyboardListener_0(listener){
  $addKeyboardListener(this, listener);
}

function getCursorPos_0(){
  return $getCursorPos(impl_5, this.getElement());
}

function getSelectionLength_0(){
  return $getSelectionLength(impl_5, this.getElement());
}

function onBrowserEvent_12(event_0){
  var type;
  $onBrowserEvent(this, event_0);
  type = eventGetType(event_0);
  if (this.keyboardListeners !== null && (type & 896) != 0) {
    $fireKeyboardEvent(this.keyboardListeners, this, event_0);
  }
   else if (type == 1) {
    if (this.clickListeners !== null) {
      $fireClick(this.clickListeners, this);
    }
  }
   else {
  }
}

function TextBoxBase(){
}

_ = TextBoxBase.prototype = new FocusWidget();
_.addClickListener = addClickListener_0;
_.addKeyboardListener = addKeyboardListener_0;
_.getCursorPos = getCursorPos_0;
_.getSelectionLength = getSelectionLength_0;
_.onBrowserEvent = onBrowserEvent_12;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TextBoxBase';
_.typeId$ = 128;
_.clickListeners = null;
_.keyboardListeners = null;
var impl_5;
function $clinit_145(){
  $clinit_145 = nullMethod;
  $clinit_197();
}

function $PasswordTextBox(this$static){
  $clinit_145();
  $TextBoxBase(this$static, createInputPassword());
  $setStyleName(this$static, 'gwt-PasswordTextBox');
  return this$static;
}

function PasswordTextBox(){
}

_ = PasswordTextBox.prototype = new TextBoxBase();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'PasswordTextBox';
_.typeId$ = 129;
function $PopupListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $firePopupClosed(this$static, sender, autoClosed){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 21);
    listener.onPopupClosed(sender, autoClosed);
  }
}

function PopupListenerCollection(){
}

_ = PopupListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'PopupListenerCollection';
_.typeId$ = 130;
function $PrefixTree(this$static){
  $PrefixTree_1(this$static, 2, null);
  return this$static;
}

function $PrefixTree_0(this$static, prefixLength){
  $PrefixTree_1(this$static, prefixLength, null);
  return this$static;
}

function $PrefixTree_1(this$static, prefixLength, source){
  this$static.prefixLength = prefixLength;
  $clear_1(this$static);
  return this$static;
}

function $add_5(this$static, s){
  var suffixes = this$static.suffixes;
  var subtrees = this$static.subtrees_0;
  var prefixLength = this$static.prefixLength;
  if (s == null || s.length == 0) {
    return false;
  }
  if (s.length <= prefixLength) {
    var safeKey = safe_0(s);
    if (suffixes.hasOwnProperty(safeKey)) {
      return false;
    }
     else {
      this$static.size++;
      suffixes[safeKey] = true;
      return true;
    }
  }
   else {
    var prefix = safe_0(s.slice(0, prefixLength));
    var theTree;
    if (subtrees.hasOwnProperty(prefix)) {
      theTree = subtrees[prefix];
    }
     else {
      theTree = createPrefixTree(prefixLength * 2);
      subtrees[prefix] = theTree;
    }
    var slice = s.slice(prefixLength);
    if (theTree.add_1(slice)) {
      this$static.size++;
      return true;
    }
     else {
      return false;
    }
  }
}

function $clear_1(this$static){
  this$static.size = 0;
  this$static.subtrees_0 = {};
  this$static.suffixes = {};
}

function $contains(this$static, s){
  return $contains_0($getSuggestions(this$static, s, 1), s);
}

function $getSuggestions(this$static, search, limit){
  var toReturn;
  toReturn = $ArrayList(new ArrayList());
  if (search !== null && limit > 0) {
    $suggestImpl(this$static, search, '', toReturn, limit);
  }
  return toReturn;
}

function $iterator_0(this$static){
  return $PrefixTree$PrefixTreeIterator(new PrefixTree$PrefixTreeIterator(), this$static);
}

function $suggestImpl(this$static, search, prefix, output, limit){
  var suffixes = this$static.suffixes;
  var subtrees = this$static.subtrees_0;
  var prefixLength = this$static.prefixLength;
  if (search.length > prefix.length + prefixLength) {
    var key_0 = safe_0(search.slice(prefix.length, prefix.length + prefixLength));
    if (subtrees.hasOwnProperty(key_0)) {
      var subtree = subtrees[key_0];
      var target = prefix + unsafe_0(key_0);
      subtree.suggestImpl(search, target, output, limit);
    }
  }
   else {
    for (suffix_0 in suffixes) {
      var target = prefix + unsafe_0(suffix_0);
      if (target.indexOf(search) == 0) {
        output.add_0(target);
      }
      if (output.size_0() >= limit) {
        return;
      }
    }
    for (var key_0 in subtrees) {
      var target = prefix + unsafe_0(key_0);
      var subtree = subtrees[key_0];
      if (target.indexOf(search) == 0) {
        if (subtree.size <= limit - output.size_0() || subtree.size == 1) {
          subtree.dump(output, target);
        }
         else {
          for (var suffix_0 in subtree.suffixes) {
            output.add_0(target + unsafe_0(suffix_0));
          }
          for (var subkey in subtree.subtrees_0) {
            output.add_0(target + unsafe_0(subkey) + '...');
          }
        }
      }
    }
  }
}

function add_0(o){
  if (instanceOf(o, 1)) {
    return $add_5(this, dynamicCast(o, 1));
  }
   else {
    throw $UnsupportedOperationException(new UnsupportedOperationException(), 'Cannot add non-Strings to PrefixTree');
  }
}

function add_1(s){
  return $add_5(this, s);
}

function contains(o){
  if (instanceOf(o, 1)) {
    return $contains(this, dynamicCast(o, 1));
  }
   else {
    return false;
  }
}

function createPrefixTree(prefixLength){
  return $PrefixTree_0(new PrefixTree(), prefixLength);
}

function dump(output, prefix){
  var i;
  for (i = $iterator_0(this); $hasNext_1(i);) {
    output.add_0(prefix + dynamicCast($next_0(i), 1));
  }
}

function iterator_2(){
  return $iterator_0(this);
}

function safe_0(s){
  return charToString(58) + s;
}

function size_0(){
  return this.size;
}

function suggestImpl(search, prefix, output, limit){
  $suggestImpl(this, search, prefix, output, limit);
}

function unsafe_0(s){
  return $substring(s, 1);
}

function PrefixTree(){
}

_ = PrefixTree.prototype = new AbstractCollection();
_.add_0 = add_0;
_.add_1 = add_1;
_.contains = contains;
_.dump = dump;
_.iterator_0 = iterator_2;
_.size_0 = size_0;
_.suggestImpl = suggestImpl;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'PrefixTree';
_.typeId$ = 131;
_.prefixLength = 0;
_.size = 0;
_.subtrees_0 = null;
_.suffixes = null;
function $PrefixTree$PrefixTreeIterator(this$static, tree){
  $init_5(this$static);
  $addTree(this$static, tree, '');
  return this$static;
}

function $addTree(this$static, tree, prefix){
  var suffixes = [];
  for (suffix in tree.suffixes) {
    suffixes.push(suffix);
  }
  var frame = {'suffixNames':suffixes, 'subtrees':tree.subtrees_0, 'prefix':prefix, 'index':0};
  var stack = this$static.stack;
  stack.push(frame);
}

function $hasNext_1(this$static){
  return $nextImpl(this$static, true) !== null;
}

function $init_5(this$static){
  this$static.stack = [];
}

function $next_0(this$static){
  var toReturn;
  toReturn = $nextImpl(this$static, false);
  if (toReturn === null) {
    if (!$hasNext_1(this$static)) {
      throw $NoSuchElementException(new NoSuchElementException(), 'No more elements in the iterator');
    }
     else {
      throw $RuntimeException(new RuntimeException(), 'nextImpl() returned null, but hasNext says otherwise');
    }
  }
  return toReturn;
}

function $nextImpl(this$static, peek){
  var stack = this$static.stack;
  var safe = safe_0;
  var unsafe = unsafe_0;
  while (stack.length > 0) {
    var frame = stack.pop();
    if (frame.index < frame.suffixNames.length) {
      var toReturn = frame.prefix + unsafe(frame.suffixNames[frame.index]);
      if (!peek) {
        frame.index++;
      }
      if (frame.index < frame.suffixNames.length) {
        stack.push(frame);
      }
       else {
        for (key in frame.subtrees) {
          var target = frame.prefix + unsafe(key);
          var subtree = frame.subtrees[key];
          this$static.addTree(subtree, target);
        }
      }
      return toReturn;
    }
     else {
      for (key in frame.subtrees) {
        var target = frame.prefix + unsafe(key);
        var subtree = frame.subtrees[key];
        this$static.addTree(subtree, target);
      }
    }
  }
  return null;
}

function addTree(tree, prefix){
  $addTree(this, tree, prefix);
}

function hasNext_1(){
  return $hasNext_1(this);
}

function next_2(){
  return $next_0(this);
}

function PrefixTree$PrefixTreeIterator(){
}

_ = PrefixTree$PrefixTreeIterator.prototype = new Object_0();
_.addTree = addTree;
_.hasNext = hasNext_1;
_.next_0 = next_2;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'PrefixTree$PrefixTreeIterator';
_.typeId$ = 132;
_.stack = null;
function $clinit_152(){
  $clinit_152 = nullMethod;
  $clinit_217() , implWidget;
}

function $$init_19(this$static){
  {
    $setStyleName(this$static, 'gwt-PushButton');
  }
}

function $PushButton(this$static, upImage){
  $clinit_217() , implWidget;
  $CustomButton_0(this$static, upImage);
  $$init_19(this$static);
  return this$static;
}

function onClick_4(){
  this.setDown(false);
  $onClick(this);
}

function onClickCancel_0(){
  this.setDown(false);
}

function onClickStart_0(){
  this.setDown(true);
}

function PushButton(){
}

_ = PushButton.prototype = new CustomButton();
_.onClick = onClick_4;
_.onClickCancel = onClickCancel_0;
_.onClickStart = onClickStart_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'PushButton';
_.typeId$ = 133;
function $clinit_153(){
  $clinit_153 = nullMethod;
  $clinit_217() , implWidget;
}

function $RadioButton(this$static, name){
  $clinit_217() , implWidget;
  $CheckBox_0(this$static, createInputRadio(name));
  $setStyleName(this$static, 'gwt-RadioButton');
  return this$static;
}

function $RadioButton_0(this$static, name, label){
  $clinit_217() , implWidget;
  $RadioButton(this$static, name);
  $setText(this$static, label);
  return this$static;
}

function RadioButton(){
}

_ = RadioButton.prototype = new CheckBox();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'RadioButton';
_.typeId$ = 134;
function $clinit_158(){
  $clinit_158 = nullMethod;
  $clinit_217() , implWidget;
}

function $$init_20(this$static){
  this$static.impl = $RichTextAreaImplSafari(new RichTextAreaImplSafari());
}

function $RichTextArea(this$static){
  $clinit_217() , implWidget;
  $FocusWidget(this$static);
  $$init_20(this$static);
  $setElement(this$static, this$static.impl.elem);
  $setStyleName(this$static, 'gwt-RichTextArea');
  return this$static;
}

function $getBasicFormatter(this$static){
  if (this$static.impl !== null) {
    return this$static.impl;
  }
  return null;
}

function $getExtendedFormatter(this$static){
  if (this$static.impl !== null && ($clinit_219() , extendedEditingSupported)) {
    return this$static.impl;
  }
  return null;
}

function onAttach_1(){
  $onAttach(this);
  $initElement(this.impl);
}

function onBrowserEvent_9(event_0){
  switch (eventGetType(event_0)) {
    case 4:
    case 8:
    case 64:
    case 16:
    case 32:
      break;
    default:$onBrowserEvent(this, event_0);
  }
}

function onDetach_3(){
  $onDetach(this);
  $uninitElement(this.impl);
}

function RichTextArea(){
}

_ = RichTextArea.prototype = new FocusWidget();
_.onAttach = onAttach_1;
_.onBrowserEvent = onBrowserEvent_9;
_.onDetach = onDetach_3;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'RichTextArea';
_.typeId$ = 135;
function $clinit_156(){
  $clinit_156 = nullMethod;
  XX_SMALL = $RichTextArea$FontSize(new RichTextArea$FontSize(), 1);
  X_SMALL = $RichTextArea$FontSize(new RichTextArea$FontSize(), 2);
  SMALL = $RichTextArea$FontSize(new RichTextArea$FontSize(), 3);
  MEDIUM = $RichTextArea$FontSize(new RichTextArea$FontSize(), 4);
  LARGE = $RichTextArea$FontSize(new RichTextArea$FontSize(), 5);
  X_LARGE = $RichTextArea$FontSize(new RichTextArea$FontSize(), 6);
  XX_LARGE = $RichTextArea$FontSize(new RichTextArea$FontSize(), 7);
}

function $RichTextArea$FontSize(this$static, number){
  $clinit_156();
  this$static.number = number;
  return this$static;
}

function toString_5(){
  return toString_9(this.number);
}

function RichTextArea$FontSize(){
}

_ = RichTextArea$FontSize.prototype = new Object_0();
_.toString$ = toString_5;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'RichTextArea$FontSize';
_.typeId$ = 136;
_.number = 0;
var LARGE, MEDIUM, SMALL, XX_LARGE, XX_SMALL, X_LARGE, X_SMALL;
function $clinit_157(){
  $clinit_157 = nullMethod;
  CENTER_0 = $RichTextArea$Justification(new RichTextArea$Justification(), 'Center');
  LEFT = $RichTextArea$Justification(new RichTextArea$Justification(), 'Left');
  RIGHT = $RichTextArea$Justification(new RichTextArea$Justification(), 'Right');
}

function $RichTextArea$Justification(this$static, tag){
  $clinit_157();
  this$static.tag = tag;
  return this$static;
}

function toString_6(){
  return 'Justify ' + this.tag;
}

function RichTextArea$Justification(){
}

_ = RichTextArea$Justification.prototype = new Object_0();
_.toString$ = toString_6;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'RichTextArea$Justification';
_.typeId$ = 137;
_.tag = null;
var CENTER_0, LEFT, RIGHT;
function $clinit_160(){
  $clinit_160 = nullMethod;
  rootPanels = $HashMap(new HashMap());
}

function $RootPanel(this$static, elem){
  $clinit_160();
  $AbsolutePanel(this$static);
  if (elem === null) {
    elem = getBodyElement();
  }
  this$static.setElement(elem);
  this$static.onAttach();
  return this$static;
}

function get(){
  $clinit_160();
  return get_0(null);
}

function get_0(id){
  $clinit_160();
  var elem, gwt;
  gwt = dynamicCast($get_1(rootPanels, id), 22);
  if (gwt !== null) {
    return gwt;
  }
  elem = null;
  if (rootPanels.size == 0) {
    hookWindowClosing_0();
  }
  $put(rootPanels, id, gwt = $RootPanel(new RootPanel(), elem));
  return gwt;
}

function getBodyElement(){
  $clinit_160();
  return $doc.body;
}

function hookWindowClosing_0(){
  $clinit_160();
  addWindowCloseListener(new RootPanel$1());
}

function RootPanel(){
}

_ = RootPanel.prototype = new AbsolutePanel();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'RootPanel';
_.typeId$ = 138;
var rootPanels;
function onWindowClosed_0(){
  var gwt, it;
  for (it = $iterator_5($values(($clinit_160() , rootPanels))); $hasNext_6(it);) {
    gwt = dynamicCast($next_5(it), 22);
    if (gwt.isAttached()) {
      gwt.onDetach();
    }
  }
}

function onWindowClosing_0(){
  return null;
}

function RootPanel$1(){
}

_ = RootPanel$1.prototype = new Object_0();
_.onWindowClosed = onWindowClosed_0;
_.onWindowClosing = onWindowClosing_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'RootPanel$1';
_.typeId$ = 139;
function $ScrollPanel(this$static){
  $SimplePanel(this$static);
  $setAlwaysShowScrollBars(this$static, false);
  $sinkEvents_0(this$static, 16384);
  return this$static;
}

function $ScrollPanel_0(this$static, child){
  $ScrollPanel(this$static);
  this$static.setWidget(child);
  return this$static;
}

function $setAlwaysShowScrollBars(this$static, alwaysShow){
  setStyleAttribute(this$static.getElement(), 'overflow', alwaysShow?'scroll':'auto');
}

function onBrowserEvent_10(event_0){
  eventGetType(event_0) == 16384;
}

function ScrollPanel(){
}

_ = ScrollPanel.prototype = new SimplePanel();
_.onBrowserEvent = onBrowserEvent_10;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ScrollPanel';
_.typeId$ = 140;
function $$init_21(this$static){
  this$static.hasElement = this$static.this$0.widget !== null;
}

function $SimplePanel$1(this$static, this$0){
  this$static.this$0 = this$0;
  $$init_21(this$static);
  return this$static;
}

function hasNext_2(){
  return this.hasElement;
}

function next_3(){
  if (!this.hasElement || this.this$0.widget === null) {
    throw new NoSuchElementException();
  }
  this.hasElement = false;
  return this.this$0.widget;
}

function SimplePanel$1(){
}

_ = SimplePanel$1.prototype = new Object_0();
_.hasNext = hasNext_2;
_.next_0 = next_3;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SimplePanel$1';
_.typeId$ = 141;
function $$init_23(this$static){
  this$static.callBack = $SuggestBox$1(new SuggestBox$1(), this$static);
}

function $SuggestBox(this$static, oracle){
  $SuggestBox_0(this$static, oracle, $TextBox(new TextBox()));
  return this$static;
}

function $SuggestBox_0(this$static, oracle, box){
  $$init_23(this$static);
  this$static.box = box;
  $initWidget(this$static, box);
  this$static.suggestionMenu = $SuggestBox$SuggestionMenu(new SuggestBox$SuggestionMenu(), true);
  this$static.suggestionPopup = $SuggestBox$SuggestionPopup(new SuggestBox$SuggestionPopup(), this$static);
  $addKeyboardSupport(this$static);
  $setOracle(this$static, oracle);
  $setStyleName(this$static, 'gwt-SuggestBox');
  return this$static;
}

function $addKeyboardSupport(this$static){
  $addKeyboardListener(this$static.box, $SuggestBox$4(new SuggestBox$4(), this$static));
}

function $setNewSelection(this$static, menuItem){
  var curSuggestion;
  curSuggestion = menuItem.suggestion;
  this$static.currentText = curSuggestion.getReplacementString();
  $setText_5(this$static.box, this$static.currentText);
  $hide(this$static.suggestionPopup);
}

function $setOracle(this$static, oracle){
  this$static.oracle = oracle;
}

function $showSuggestions_0(this$static, suggestions){
  var curSuggestion, menuItem, suggestionsIter;
  if (suggestions.size > 0) {
    $setVisible(this$static.suggestionPopup, false);
    $clearItems(this$static.suggestionMenu);
    suggestionsIter = $iterator_3(suggestions);
    while ($hasNext_4(suggestionsIter)) {
      curSuggestion = dynamicCast($next_3(suggestionsIter), 30);
      menuItem = $SuggestBox$SuggestionMenuItem(new SuggestBox$SuggestionMenuItem(), curSuggestion, true);
      $setCommand(menuItem, $SuggestBox$3(new SuggestBox$3(), this$static, menuItem));
      $addItem_1(this$static.suggestionMenu, menuItem);
    }
    $selectItem_0(this$static.suggestionMenu, 0);
    $showAlignedPopup(this$static.suggestionPopup);
  }
   else {
    $hide(this$static.suggestionPopup);
  }
}

function $showSuggestions(this$static, query){
  $requestSuggestions(this$static.oracle, $SuggestOracle$Request(new SuggestOracle$Request(), query, this$static.limit), this$static.callBack);
}

function SuggestBox(){
}

_ = SuggestBox.prototype = new Composite();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestBox';
_.typeId$ = 142;
_.box = null;
_.currentText = null;
_.limit = 20;
_.oracle = null;
_.suggestionMenu = null;
_.suggestionPopup = null;
function $SuggestBox$1(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $onSuggestionsReady(this$static, request, response){
  $showSuggestions_0(this$static.this$0, response.suggestions);
}

function SuggestBox$1(){
}

_ = SuggestBox$1.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestBox$1';
_.typeId$ = 143;
function $SuggestBox$2(this$static, this$1){
  this$static.this$1 = this$1;
  return this$static;
}

function $setPosition(this$static, offsetWidth, offsetHeight){
  var distanceFromWindowLeft, distanceFromWindowTop, distanceToWindowBottom, distanceToWindowRight, left, offsetWidthDiff, top, windowBottom, windowLeft, windowRight, windowTop;
  left = $getAbsoluteLeft_0(this$static.this$1.this$0.box);
  offsetWidthDiff = offsetWidth - this$static.this$1.this$0.box.getOffsetWidth();
  if (offsetWidthDiff > 0) {
    windowRight = getClientWidth() + getScrollLeft();
    windowLeft = getScrollLeft();
    distanceToWindowRight = windowRight - left;
    distanceFromWindowLeft = left - windowLeft;
    if (distanceToWindowRight < offsetWidth && distanceFromWindowLeft >= offsetWidth - this$static.this$1.this$0.box.getOffsetWidth()) {
      left -= offsetWidthDiff;
    }
  }
  top = $getAbsoluteTop_0(this$static.this$1.this$0.box);
  windowTop = getScrollTop();
  windowBottom = getScrollTop() + getClientHeight();
  distanceFromWindowTop = top - windowTop;
  distanceToWindowBottom = windowBottom - (top + this$static.this$1.this$0.box.getOffsetHeight());
  if (distanceToWindowBottom < offsetHeight && distanceFromWindowTop >= offsetHeight) {
    top -= offsetHeight;
  }
   else {
    top += this$static.this$1.this$0.box.getOffsetHeight();
  }
  $setPopupPosition(this$static.this$1, left, top);
}

function SuggestBox$2(){
}

_ = SuggestBox$2.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestBox$2';
_.typeId$ = 144;
function $SuggestBox$3(this$static, this$0, val$menuItem){
  this$static.this$0 = this$0;
  this$static.val$menuItem = val$menuItem;
  return this$static;
}

function execute_2(){
  $setNewSelection(this.this$0, this.val$menuItem);
}

function SuggestBox$3(){
}

_ = SuggestBox$3.prototype = new Object_0();
_.execute = execute_2;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestBox$3';
_.typeId$ = 145;
function $SuggestBox$4(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $refreshSuggestions(this$static){
  var text;
  text = $getText(this$static.this$0.box);
  if ($equals_1(text, this$static.this$0.currentText)) {
    return;
  }
   else {
    this$static.this$0.currentText = text;
  }
  if ($length(text) == 0) {
    $hide(this$static.this$0.suggestionPopup);
    $clearItems(this$static.this$0.suggestionMenu);
  }
   else {
    $showSuggestions(this$static.this$0, text);
  }
}

function onKeyDown_1(sender, keyCode, modifiers){
  if (this.this$0.suggestionPopup.isAttached()) {
    switch (keyCode) {
      case 40:
        $selectItem_0(this.this$0.suggestionMenu, $getSelectedItemIndex(this.this$0.suggestionMenu) + 1);
        break;
      case 38:
        $selectItem_0(this.this$0.suggestionMenu, $getSelectedItemIndex(this.this$0.suggestionMenu) - 1);
        break;
      case 13:
      case 9:
        $doSelectedItemAction(this.this$0.suggestionMenu);
        break;
    }
  }
}

function onKeyUp_2(sender, keyCode, modifiers){
  $refreshSuggestions(this);
}

function SuggestBox$4(){
}

_ = SuggestBox$4.prototype = new KeyboardListenerAdapter();
_.onKeyDown = onKeyDown_1;
_.onKeyUp = onKeyUp_2;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestBox$4';
_.typeId$ = 146;
function $SuggestBox$SuggestionMenu(this$static, vertical){
  $MenuBar_0(this$static, vertical);
  $setStyleName(this$static, '');
  return this$static;
}

function $doSelectedItemAction(this$static){
  var selectedItem;
  selectedItem = this$static.selectedItem;
  if (selectedItem !== null) {
    $doItemAction(this$static, selectedItem, true);
  }
}

function $getSelectedItemIndex(this$static){
  var selectedItem;
  selectedItem = this$static.selectedItem;
  if (selectedItem !== null) {
    return $indexOf_3(this$static.items, selectedItem);
  }
  return (-1);
}

function $selectItem_0(this$static, index){
  var items;
  items = this$static.items;
  if (index > (-1) && index < items.size) {
    $itemOver(this$static, dynamicCast($get_0(items, index), 31));
  }
}

function SuggestBox$SuggestionMenu(){
}

_ = SuggestBox$SuggestionMenu.prototype = new MenuBar();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestBox$SuggestionMenu';
_.typeId$ = 147;
function $SuggestBox$SuggestionMenuItem(this$static, suggestion, asHTML){
  $MenuItem_1(this$static, suggestion.getDisplayString(), asHTML);
  setStyleAttribute(this$static.getElement(), 'whiteSpace', 'nowrap');
  $setStyleName(this$static, 'item');
  $setSuggestion(this$static, suggestion);
  return this$static;
}

function $setSuggestion(this$static, suggestion){
  this$static.suggestion = suggestion;
}

function SuggestBox$SuggestionMenuItem(){
}

_ = SuggestBox$SuggestionMenuItem.prototype = new MenuItem();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestBox$SuggestionMenuItem';
_.typeId$ = 148;
_.suggestion = null;
function $clinit_183(){
  $clinit_183 = nullMethod;
  $clinit_149();
}

function $SuggestBox$SuggestionPopup(this$static, this$0){
  $clinit_183();
  this$static.this$0 = this$0;
  $PopupPanel_0(this$static, true);
  this$static.setWidget(this$static.this$0.suggestionMenu);
  $setStyleName(this$static, 'gwt-SuggestBoxPopup');
  return this$static;
}

function $showAlignedPopup(this$static){
  $setPopupPositionAndShow(this$static, $SuggestBox$2(new SuggestBox$2(), this$static));
}

function SuggestBox$SuggestionPopup(){
}

_ = SuggestBox$SuggestionPopup.prototype = new PopupPanel();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestBox$SuggestionPopup';
_.typeId$ = 149;
function $SuggestOracle$Request(this$static, query, limit){
  $setQuery(this$static, query);
  $setLimit(this$static, limit);
  return this$static;
}

function $setLimit(this$static, limit){
  this$static.limit = limit;
}

function $setQuery(this$static, query){
  this$static.query = query;
}

function SuggestOracle$Request(){
}

_ = SuggestOracle$Request.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestOracle$Request';
_.typeId$ = 150;
_.limit = 20;
_.query = null;
function $SuggestOracle$Response(this$static, suggestions){
  $setSuggestions(this$static, suggestions);
  return this$static;
}

function $setSuggestions(this$static, suggestions){
  this$static.suggestions = suggestions;
}

function SuggestOracle$Response(){
}

_ = SuggestOracle$Response.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SuggestOracle$Response';
_.typeId$ = 151;
_.suggestions = null;
function $$init_24(this$static){
  this$static.panel = $HorizontalPanel(new HorizontalPanel());
}

function $TabBar(this$static){
  var first, rest;
  $$init_24(this$static);
  $initWidget(this$static, this$static.panel);
  $sinkEvents_0(this$static, 1);
  $setStyleName(this$static, 'gwt-TabBar');
  $setVerticalAlignment_0(this$static.panel, ($clinit_112() , ALIGN_BOTTOM));
  first = $HTML_1(new HTML(), '&nbsp;', true);
  rest = $HTML_1(new HTML(), '&nbsp;', true);
  $setStyleName(first, 'gwt-TabBarFirst');
  $setStyleName(rest, 'gwt-TabBarRest');
  first.setHeight('100%');
  rest.setHeight('100%');
  $add_3(this$static.panel, first);
  $add_3(this$static.panel, rest);
  first.setHeight('100%');
  this$static.panel.setCellHeight(first, '100%');
  this$static.panel.setCellWidth(rest, '100%');
  return this$static;
}

function $addTabListener(this$static, listener){
  if (this$static.tabListeners === null) {
    this$static.tabListeners = $TabListenerCollection(new TabListenerCollection());
  }
  $add_9(this$static.tabListeners, listener);
}

function $checkInsertBeforeTabIndex(this$static, beforeIndex){
  if (beforeIndex < 0 || beforeIndex > $getTabCount(this$static)) {
    throw new IndexOutOfBoundsException();
  }
}

function $checkTabIndex(this$static, index){
  if (index < (-1) || index >= $getTabCount(this$static)) {
    throw new IndexOutOfBoundsException();
  }
}

function $getTabCount(this$static){
  return this$static.panel.children_0.size - 2;
}

function $insertTab(this$static, text, asHTML, beforeIndex){
  var item;
  $checkInsertBeforeTabIndex(this$static, beforeIndex);
  if (asHTML) {
    item = $HTML_0(new HTML(), text);
  }
   else {
    item = $Label_0(new Label(), text);
  }
  $setWordWrap(item, false);
  $addClickListener(item, this$static);
  $setStyleName(item, 'gwt-TabBarItem');
  $insert_1(this$static.panel, item, beforeIndex + 1);
}

function $removeTab(this$static, index){
  var toRemove;
  $checkTabIndex(this$static, index);
  toRemove = $getWidget(this$static.panel, index + 1);
  if (toRemove === this$static.selectedTab) {
    this$static.selectedTab = null;
  }
  $remove_4(this$static.panel, toRemove);
}

function $selectTab(this$static, index){
  $checkTabIndex(this$static, index);
  if (this$static.tabListeners !== null) {
    if (!$fireBeforeTabSelected(this$static.tabListeners, this$static, index)) {
      return false;
    }
  }
  $setSelectionStyle_0(this$static, this$static.selectedTab, false);
  if (index == (-1)) {
    this$static.selectedTab = null;
    return true;
  }
  this$static.selectedTab = $getWidget(this$static.panel, index + 1);
  $setSelectionStyle_0(this$static, this$static.selectedTab, true);
  if (this$static.tabListeners !== null) {
    $fireTabSelected(this$static.tabListeners, this$static, index);
  }
  return true;
}

function $setSelectionStyle_0(this$static, item, selected){
  if (item !== null) {
    if (selected) {
      $addStyleName(item, 'gwt-TabBarItem-selected');
    }
     else {
      $removeStyleName(item, 'gwt-TabBarItem-selected');
    }
  }
}

function onClick_5(sender){
  var i;
  for (i = 1; i < this.panel.children_0.size - 1; ++i) {
    if ($getWidget(this.panel, i) === sender) {
      $selectTab(this, i - 1);
      return;
    }
  }
}

function TabBar(){
}

_ = TabBar.prototype = new Composite();
_.onClick_0 = onClick_5;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TabBar';
_.typeId$ = 152;
_.selectedTab = null;
_.tabListeners = null;
function $TabListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireBeforeTabSelected(this$static, sender, tabIndex){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 32);
    if (!listener.onBeforeTabSelected(sender, tabIndex)) {
      return false;
    }
  }
  return true;
}

function $fireTabSelected(this$static, sender, tabIndex){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 32);
    listener.onTabSelected(sender, tabIndex);
  }
}

function TabListenerCollection(){
}

_ = TabListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TabListenerCollection';
_.typeId$ = 153;
function $$init_25(this$static){
  this$static.tabBar = $TabPanel$UnmodifiableTabBar(new TabPanel$UnmodifiableTabBar());
  this$static.deck = $TabPanel$TabbedDeckPanel(new TabPanel$TabbedDeckPanel(), this$static.tabBar);
}

function $TabPanel(this$static){
  var panel;
  $$init_25(this$static);
  panel = $VerticalPanel(new VerticalPanel());
  $add_7(panel, this$static.tabBar);
  $add_7(panel, this$static.deck);
  panel.setCellHeight(this$static.deck, '100%');
  this$static.tabBar.setWidth('100%');
  $addTabListener(this$static.tabBar, this$static);
  $initWidget(this$static, panel);
  $setStyleName(this$static, 'gwt-TabPanel');
  $setStyleName(this$static.deck, 'gwt-TabPanelBottom');
  return this$static;
}

function $add_6(this$static, w, tabText){
  $insert_2(this$static, w, tabText, this$static.deck.children_0.size);
}

function $insert_3(this$static, widget, tabText, asHTML, beforeIndex){
  $insertProtected(this$static.deck, widget, tabText, asHTML, beforeIndex);
}

function $insert_2(this$static, widget, tabText, beforeIndex){
  $insert_3(this$static, widget, tabText, false, beforeIndex);
}

function $selectTab_0(this$static, index){
  $selectTab(this$static.tabBar, index);
}

function iterator_5(){
  return $iterator(this.deck);
}

function onBeforeTabSelected(sender, tabIndex){
  return true;
}

function onTabSelected(sender, tabIndex){
  $showWidget(this.deck, tabIndex);
}

function remove_11(widget){
  return $remove_5(this.deck, widget);
}

function TabPanel(){
}

_ = TabPanel.prototype = new Composite();
_.iterator_0 = iterator_5;
_.onBeforeTabSelected = onBeforeTabSelected;
_.onTabSelected = onTabSelected;
_.remove_0 = remove_11;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TabPanel';
_.typeId$ = 154;
function $TabPanel$TabbedDeckPanel(this$static, tabBar){
  $DeckPanel(this$static);
  this$static.tabBar = tabBar;
  return this$static;
}

function $insertProtected(this$static, w, tabText, asHTML, beforeIndex){
  var idx;
  idx = $getWidgetIndex(this$static, w);
  if (idx != (-1)) {
    $remove_5(this$static, w);
    if (idx < beforeIndex) {
      beforeIndex--;
    }
  }
  $insertTabProtected(this$static.tabBar, tabText, asHTML, beforeIndex);
  $insert_0(this$static, w, beforeIndex);
}

function $remove_5(this$static, w){
  var idx;
  idx = $getWidgetIndex(this$static, w);
  if (idx != (-1)) {
    $removeTabProtected(this$static.tabBar, idx);
    return $remove_2(this$static, w);
  }
  return false;
}

function remove_10(w){
  return $remove_5(this, w);
}

function TabPanel$TabbedDeckPanel(){
}

_ = TabPanel$TabbedDeckPanel.prototype = new DeckPanel();
_.remove_0 = remove_10;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TabPanel$TabbedDeckPanel';
_.typeId$ = 155;
_.tabBar = null;
function $TabPanel$UnmodifiableTabBar(this$static){
  $TabBar(this$static);
  return this$static;
}

function $insertTabProtected(this$static, text, asHTML, beforeIndex){
  $insertTab(this$static, text, asHTML, beforeIndex);
}

function $removeTabProtected(this$static, index){
  $removeTab(this$static, index);
}

function TabPanel$UnmodifiableTabBar(){
}

_ = TabPanel$UnmodifiableTabBar.prototype = new TabBar();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TabPanel$UnmodifiableTabBar';
_.typeId$ = 156;
function $clinit_196(){
  $clinit_196 = nullMethod;
  $clinit_197();
}

function $TextArea(this$static){
  $clinit_196();
  $TextBoxBase(this$static, createTextArea());
  $setStyleName(this$static, 'gwt-TextArea');
  return this$static;
}

function $setVisibleLines(this$static, lines){
  setElementPropertyInt(this$static.getElement(), 'rows', lines);
}

function getCursorPos(){
  return $getTextAreaCursorPos(impl_5, this.getElement());
}

function getSelectionLength(){
  return $getSelectionLength(impl_5, this.getElement());
}

function TextArea(){
}

_ = TextArea.prototype = new TextBoxBase();
_.getCursorPos = getCursorPos;
_.getSelectionLength = getSelectionLength;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TextArea';
_.typeId$ = 157;
function $clinit_198(){
  $clinit_198 = nullMethod;
  $clinit_197();
}

function $TextBox(this$static){
  $clinit_198();
  $TextBoxBase(this$static, createInputText());
  $setStyleName(this$static, 'gwt-TextBox');
  return this$static;
}

function TextBox(){
}

_ = TextBox.prototype = new TextBoxBase();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TextBox';
_.typeId$ = 158;
function $clinit_199(){
  $clinit_199 = nullMethod;
  $clinit_217() , implWidget;
}

function $$init_26(this$static){
  {
    $setStyleName(this$static, STYLENAME_DEFAULT);
  }
}

function $ToggleButton(this$static, upImage){
  $clinit_217() , implWidget;
  $CustomButton_0(this$static, upImage);
  $$init_26(this$static);
  return this$static;
}

function $setDown_0(this$static, down){
  $setDown(this$static, down);
}

function isDown_0(){
  return $isDown(this);
}

function onClick_6(){
  $toggleDown(this);
  $onClick(this);
}

function setDown_0(down){
  $setDown_0(this, down);
}

function ToggleButton(){
}

_ = ToggleButton.prototype = new CustomButton();
_.isDown = isDown_0;
_.onClick = onClick_6;
_.setDown = setDown_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ToggleButton';
_.typeId$ = 159;
var STYLENAME_DEFAULT = 'gwt-ToggleButton';
function $$init_28(this$static){
  this$static.childWidgets = $HashMap(new HashMap());
}

function $Tree(this$static, images){
  $$init_28(this$static);
  this$static.images = images;
  this$static.setElement(createDiv());
  setStyleAttribute(this$static.getElement(), 'position', 'relative');
  this$static.focusable = $createFocusable(($clinit_93() , impl_1));
  setStyleAttribute(this$static.focusable, 'fontSize', '0');
  setStyleAttribute(this$static.focusable, 'position', 'absolute');
  setIntStyleAttribute(this$static.focusable, 'zIndex', (-1));
  appendChild(this$static.getElement(), this$static.focusable);
  $sinkEvents_0(this$static, 1021);
  sinkEvents(this$static.focusable, 6144);
  this$static.root = $Tree$1(new Tree$1(), this$static);
  $setTree(this$static.root, this$static);
  $setStyleName(this$static, 'gwt-Tree');
  return this$static;
}

function $addItem_6(this$static, item){
  $addItem_5(this$static.root, item);
}

function $addTreeListener(this$static, listener){
  if (this$static.listeners === null) {
    this$static.listeners = $TreeListenerCollection(new TreeListenerCollection());
  }
  $add_9(this$static.listeners, listener);
}

function $collectElementChain(this$static, chain, hRoot, hElem){
  if (hElem === null || compare_0(hElem, hRoot)) {
    return;
  }
  $collectElementChain(this$static, chain, hRoot, getParent(hElem));
  $add_9(chain, wrapJSO(hElem, Element));
}

function $elementClicked(this$static, root, hElem){
  var chain, item;
  chain = $ArrayList(new ArrayList());
  $collectElementChain(this$static, chain, this$static.getElement(), hElem);
  item = $findItemByChain(this$static, chain, 0, root);
  if (item !== null) {
    if (isOrHasChild($getImageElement(item), hElem)) {
      $setState_0(item, !item.open, true);
      return true;
    }
     else if (isOrHasChild(item.getElement(), hElem)) {
      $onSelection(this$static, item, true, !$shouldTreeDelegateFocusToElement(this$static, hElem));
      return true;
    }
  }
  return false;
}

function $findDeepestOpenChild(this$static, item){
  if (!item.open) {
    return item;
  }
  return $findDeepestOpenChild(this$static, $getChild_0(item, item.children_0.size - 1));
}

function $findItemByChain(this$static, chain, idx, root){
  var child, hCurElem, i, n, retItem;
  if (idx == chain.size) {
    return root;
  }
  hCurElem = dynamicCast($get_0(chain, idx), 8);
  for (i = 0 , n = root.children_0.size; i < n; ++i) {
    child = $getChild_0(root, i);
    if (compare_0(child.getElement(), hCurElem)) {
      retItem = $findItemByChain(this$static, chain, idx + 1, $getChild_0(root, i));
      if (retItem === null) {
        return child;
      }
      return retItem;
    }
  }
  return $findItemByChain(this$static, chain, idx + 1, root);
}

function $fireStateChanged(this$static, item){
  if (this$static.listeners !== null) {
    $fireItemStateChanged(this$static.listeners, item);
  }
}

function $iterator_1(this$static){
  var widgets;
  widgets = initDims_0('[Lcom.google.gwt.user.client.ui.Widget;', [204], [15], [this$static.childWidgets.size], null);
  $keySet(this$static.childWidgets).toArray_0(widgets);
  return createWidgetIterator(this$static, widgets);
}

function $moveFocus(this$static, selection){
  var containerLeft, containerTop, focusableWidget, height, left, selectedElem, top, width;
  focusableWidget = $getFocusableWidget(selection);
  {
    selectedElem = selection.contentElem;
    containerLeft = $getAbsoluteLeft_0(this$static);
    containerTop = $getAbsoluteTop_0(this$static);
    left = getAbsoluteLeft(selectedElem) - containerLeft;
    top = getAbsoluteTop(selectedElem) - containerTop;
    width = getElementPropertyInt(selectedElem, 'offsetWidth');
    height = getElementPropertyInt(selectedElem, 'offsetHeight');
    setIntStyleAttribute(this$static.focusable, 'left', left);
    setIntStyleAttribute(this$static.focusable, 'top', top);
    setIntStyleAttribute(this$static.focusable, 'width', width);
    setIntStyleAttribute(this$static.focusable, 'height', height);
    scrollIntoView(this$static.focusable);
    $focus(($clinit_93() , impl_1), this$static.focusable);
  }
}

function $moveSelectionDown(this$static, sel, dig){
  var idx, parent;
  if (sel === this$static.root) {
    return;
  }
  parent = sel.parent;
  if (parent === null) {
    parent = this$static.root;
  }
  idx = $getChildIndex(parent, sel);
  if (!dig || !sel.open) {
    if (idx < parent.children_0.size - 1) {
      $onSelection(this$static, $getChild_0(parent, idx + 1), true, true);
    }
     else {
      $moveSelectionDown(this$static, parent, false);
    }
  }
   else if (sel.children_0.size > 0) {
    $onSelection(this$static, $getChild_0(sel, 0), true, true);
  }
}

function $moveSelectionUp(this$static, sel){
  var idx, parent, sibling;
  parent = sel.parent;
  if (parent === null) {
    parent = this$static.root;
  }
  idx = $getChildIndex(parent, sel);
  if (idx > 0) {
    sibling = $getChild_0(parent, idx - 1);
    $onSelection(this$static, $findDeepestOpenChild(this$static, sibling), true, true);
  }
   else {
    $onSelection(this$static, parent, true, true);
  }
}

function $onSelection(this$static, item, fireEvents, moveFocus){
  if (item === this$static.root) {
    return;
  }
  if (this$static.curSelection !== null) {
    $setSelected(this$static.curSelection, false);
  }
  this$static.curSelection = item;
  if (moveFocus && this$static.curSelection !== null) {
    $moveFocus(this$static, this$static.curSelection);
    $setSelected(this$static.curSelection, true);
    if (fireEvents && this$static.listeners !== null) {
      $fireItemSelected(this$static.listeners, this$static.curSelection);
    }
  }
}

function $removeItem_0(this$static, item){
  $removeItem(this$static.root, item);
}

function $setFocus_0(this$static, focus){
  if (focus) {
    $focus(($clinit_93() , impl_1), this$static.focusable);
  }
   else {
    $blur_0(($clinit_93() , impl_1), this$static.focusable);
  }
}

function $setSelectedItem(this$static, item){
  $setSelectedItem_0(this$static, item, true);
}

function $setSelectedItem_0(this$static, item, fireEvents){
  if (item === null) {
    if (this$static.curSelection === null) {
      return;
    }
    $setSelected(this$static.curSelection, false);
    this$static.curSelection = null;
    return;
  }
  $onSelection(this$static, item, fireEvents, true);
}

function $shouldTreeDelegateFocusToElement(this$static, elem){
  var name = elem.nodeName;
  return name == 'SELECT' || (name == 'INPUT' || (name == 'TEXTAREA' || (name == 'OPTION' || (name == 'BUTTON' || name == 'LABEL'))));
}

function doAttachChildren_0(){
  var child, it;
  for (it = $iterator_1(this); $hasNext_3(it);) {
    child = $next_2(it);
    child.onAttach();
  }
  setEventListener(this.focusable, this);
}

function doDetachChildren_0(){
  var child, it;
  for (it = $iterator_1(this); $hasNext_3(it);) {
    child = $next_2(it);
    child.onDetach();
  }
  setEventListener(this.focusable, null);
}

function iterator_6(){
  return $iterator_1(this);
}

function onBrowserEvent_13(event_0){
  var chain, e, eventType, item, parent;
  eventType = eventGetType(event_0);
  switch (eventType) {
    case 1:
      {
        e = eventGetTarget(event_0);
        if ($shouldTreeDelegateFocusToElement(this, e)) {
        }
         else {
          $setFocus_0(this, true);
        }
        break;
      }

    case 4:
      {
        if ($equals_0(eventGetCurrentTarget(event_0), wrapJSO(this.getElement(), Element))) {
          $elementClicked(this, this.root, eventGetTarget(event_0));
        }
        break;
      }

    case 8:
      {
        break;
      }

    case 64:
      {
        break;
      }

    case 16:
      {
        break;
      }

    case 32:
      {
        break;
      }

    case 2048:
      break;
    case 4096:
      {
        break;
      }

    case 128:
      if (this.curSelection === null) {
        if (this.root.children_0.size > 0) {
          $onSelection(this, $getChild_0(this.root, 0), true, true);
        }
        return;
      }

      if (this.lastEventType == 128) {
        return;
      }

      {
        switch (eventGetKeyCode(event_0)) {
          case 38:
            {
              $moveSelectionUp(this, this.curSelection);
              eventPreventDefault(event_0);
              break;
            }

          case 40:
            {
              $moveSelectionDown(this, this.curSelection, true);
              eventPreventDefault(event_0);
              break;
            }

          case 37:
            {
              if (this.curSelection.open) {
                $setState(this.curSelection, false);
              }
               else {
                parent = this.curSelection.parent;
                if (parent !== null) {
                  $setSelectedItem(this, parent);
                }
              }
              eventPreventDefault(event_0);
              break;
            }

          case 39:
            {
              if (!this.curSelection.open) {
                $setState(this.curSelection, true);
              }
               else if (this.curSelection.children_0.size > 0) {
                $setSelectedItem(this, $getChild_0(this.curSelection, 0));
              }
              eventPreventDefault(event_0);
              break;
            }

        }
      }

    case 512:
      if (eventType == 512) {
        if (eventGetKeyCode(event_0) == 9) {
          chain = $ArrayList(new ArrayList());
          $collectElementChain(this, chain, this.getElement(), eventGetTarget(event_0));
          item = $findItemByChain(this, chain, 0, this.root);
          if (item !== this.curSelection) {
            $setSelectedItem_0(this, item, true);
          }
        }
      }

    case 256:
      {
        break;
      }

  }
  this.lastEventType = eventType;
}

function onLoad_2(){
  $updateStateRecursive(this.root);
}

function remove_12(w){
  var item;
  item = dynamicCast($get_1(this.childWidgets, w), 33);
  if (item === null) {
    return false;
  }
  $setWidget_4(item, null);
  return true;
}

function Tree(){
}

_ = Tree.prototype = new Widget();
_.doAttachChildren = doAttachChildren_0;
_.doDetachChildren = doDetachChildren_0;
_.iterator_0 = iterator_6;
_.onBrowserEvent = onBrowserEvent_13;
_.onLoad = onLoad_2;
_.remove_0 = remove_12;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Tree';
_.typeId$ = 160;
_.curSelection = null;
_.focusable = null;
_.images = null;
_.lastEventType = 0;
_.listeners = null;
_.root = null;
function $Tree$1(this$static, this$0){
  this$static.this$0 = this$0;
  $TreeItem(this$static);
  return this$static;
}

function $addItem_5(this$static, item){
  if (item.parent !== null || item.tree !== null) {
    $remove_6(item);
  }
  appendChild(this$static.this$0.getElement(), item.getElement());
  $setTree(item, this$static.tree);
  $setParentItem(item, null);
  $add_9(this$static.children_0, item);
  setIntStyleAttribute(item.getElement(), 'marginLeft', 0);
}

function $removeItem(this$static, item){
  if (!$contains_0(this$static.children_0, item)) {
    return;
  }
  $setTree(item, null);
  $setParentItem(item, null);
  $remove_13(this$static.children_0, item);
  removeChild(this$static.this$0.getElement(), item.getElement());
}

function addItem(item){
  $addItem_5(this, item);
}

function removeItem(item){
  $removeItem(this, item);
}

function Tree$1(){
}

_ = Tree$1.prototype = new TreeItem();
_.addItem = addItem;
_.removeItem = removeItem;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Tree$1';
_.typeId$ = 161;
function $TreeListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireItemSelected(this$static, item){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 34);
    listener.onTreeItemSelected(item);
  }
}

function $fireItemStateChanged(this$static, item){
  var it, listener;
  for (it = $iterator_3(this$static); $hasNext_4(it);) {
    listener = dynamicCast($next_3(it), 34);
    listener.onTreeItemStateChanged(item);
  }
}

function TreeListenerCollection(){
}

_ = TreeListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TreeListenerCollection';
_.typeId$ = 162;
function $$init_29(this$static){
  this$static.horzAlign = ($clinit_108() , ALIGN_LEFT);
  this$static.vertAlign = ($clinit_112() , ALIGN_TOP);
}

function $VerticalPanel(this$static){
  $CellPanel(this$static);
  $$init_29(this$static);
  setElementProperty(this$static.table, 'cellSpacing', '0');
  setElementProperty(this$static.table, 'cellPadding', '0');
  return this$static;
}

function $add_7(this$static, w){
  var td, tr;
  tr = createTR();
  td = $createAlignedTd_0(this$static);
  appendChild(tr, td);
  appendChild(this$static.body_0, tr);
  $add_0(this$static, w, td);
}

function $createAlignedTd_0(this$static){
  var td;
  td = createTD();
  $setCellHorizontalAlignment(this$static, td, this$static.horzAlign);
  $setCellVerticalAlignment(this$static, td, this$static.vertAlign);
  return td;
}

function $remove_7(this$static, w){
  var removed, td;
  td = getParent(w.getElement());
  removed = $remove_1(this$static, w);
  if (removed) {
    removeChild(this$static.body_0, getParent(td));
  }
  return removed;
}

function $setHorizontalAlignment_1(this$static, align){
  this$static.horzAlign = align;
}

function remove_13(w){
  return $remove_7(this, w);
}

function VerticalPanel(){
}

_ = VerticalPanel.prototype = new CellPanel();
_.remove_0 = remove_13;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'VerticalPanel';
_.typeId$ = 163;
function $WidgetCollection(this$static, parent){
  this$static.array = initDims_0('[Lcom.google.gwt.user.client.ui.Widget;', [204], [15], [4], null);
  return this$static;
}

function $add_8(this$static, w){
  $insert_4(this$static, w, this$static.size);
}

function $get(this$static, index){
  if (index < 0 || index >= this$static.size) {
    throw new IndexOutOfBoundsException();
  }
  return this$static.array[index];
}

function $indexOf(this$static, w){
  var i;
  for (i = 0; i < this$static.size; ++i) {
    if (this$static.array[i] === w) {
      return i;
    }
  }
  return (-1);
}

function $insert_4(this$static, w, beforeIndex){
  var i, newArray;
  if (beforeIndex < 0 || beforeIndex > this$static.size) {
    throw new IndexOutOfBoundsException();
  }
  if (this$static.size == this$static.array.length_0) {
    newArray = initDims_0('[Lcom.google.gwt.user.client.ui.Widget;', [204], [15], [this$static.array.length_0 * 2], null);
    for (i = 0; i < this$static.array.length_0; ++i) {
      setCheck(newArray, i, this$static.array[i]);
    }
    this$static.array = newArray;
  }
  ++this$static.size;
  for (i = this$static.size - 1; i > beforeIndex; --i) {
    setCheck(this$static.array, i, this$static.array[i - 1]);
  }
  setCheck(this$static.array, beforeIndex, w);
}

function $iterator_2(this$static){
  return $WidgetCollection$WidgetIterator(new WidgetCollection$WidgetIterator(), this$static);
}

function $remove_8(this$static, index){
  var i;
  if (index < 0 || index >= this$static.size) {
    throw new IndexOutOfBoundsException();
  }
  --this$static.size;
  for (i = index; i < this$static.size; ++i) {
    setCheck(this$static.array, i, this$static.array[i + 1]);
  }
  setCheck(this$static.array, this$static.size, null);
}

function $remove_9(this$static, w){
  var index;
  index = $indexOf(this$static, w);
  if (index == (-1)) {
    throw new NoSuchElementException();
  }
  $remove_8(this$static, index);
}

function WidgetCollection(){
}

_ = WidgetCollection.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'WidgetCollection';
_.typeId$ = 164;
_.array = null;
_.size = 0;
function $WidgetCollection$WidgetIterator(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $hasNext_2(this$static){
  return this$static.index_0 < this$static.this$0.size - 1;
}

function $next_1(this$static){
  if (this$static.index_0 >= this$static.this$0.size) {
    throw new NoSuchElementException();
  }
  return this$static.this$0.array[++this$static.index_0];
}

function hasNext_3(){
  return $hasNext_2(this);
}

function next_4(){
  return $next_1(this);
}

function WidgetCollection$WidgetIterator(){
}

_ = WidgetCollection$WidgetIterator.prototype = new Object_0();
_.hasNext = hasNext_3;
_.next_0 = next_4;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'WidgetCollection$WidgetIterator';
_.typeId$ = 165;
_.index_0 = (-1);
function createWidgetIterator(container, contained){
  return $WidgetIterators$1(new WidgetIterators$1(), contained, container);
}

function $$init_30(this$static){
  {
    $gotoNextIndex(this$static);
  }
}

function $WidgetIterators$1(this$static, val$contained, val$container){
  this$static.val$contained = val$contained;
  $$init_30(this$static);
  return this$static;
}

function $gotoNextIndex(this$static){
  ++this$static.index_0;
  while (this$static.index_0 < this$static.val$contained.length_0) {
    if (this$static.val$contained[this$static.index_0] !== null) {
      return;
    }
    ++this$static.index_0;
  }
}

function $hasNext_3(this$static){
  return this$static.index_0 < this$static.val$contained.length_0;
}

function $next_2(this$static){
  var w;
  if (!$hasNext_3(this$static)) {
    throw new NoSuchElementException();
  }
  w = this$static.val$contained[this$static.index_0];
  $gotoNextIndex(this$static);
  return w;
}

function hasNext_4(){
  return $hasNext_3(this);
}

function next_5(){
  return $next_2(this);
}

function WidgetIterators$1(){
}

_ = WidgetIterators$1.prototype = new Object_0();
_.hasNext = hasNext_4;
_.next_0 = next_5;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'WidgetIterators$1';
_.typeId$ = 166;
_.index_0 = (-1);
function $adjust(this$static, img, url, left, top, width, height){
  var style;
  style = 'url(' + url + ') no-repeat ' + (-left + 'px ') + (-top + 'px');
  setStyleAttribute(img, 'background', style);
  setStyleAttribute(img, 'width', width + 'px');
  setStyleAttribute(img, 'height', height + 'px');
}

function $createStructure(this$static, url, left, top, width, height){
  var tmp;
  tmp = createSpan();
  setInnerHTML(tmp, $getHTML_0(this$static, url, left, top, width, height));
  return getFirstChild(tmp);
}

function $getHTML_0(this$static, url, left, top, width, height){
  var clippedImgHtml, style;
  style = 'width: ' + width + 'px; height: ' + height + 'px; background: url(' + url + ') no-repeat ' + (-left + 'px ') + (-top + 'px');
  clippedImgHtml = "<img src='" + getModuleBaseURL() + "clear.cache.gif' style='" + style + "' border='0'>";
  return clippedImgHtml;
}

function ClippedImageImpl(){
}

_ = ClippedImageImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'ClippedImageImpl';
_.typeId$ = 167;
function $clinit_214(){
  $clinit_214 = nullMethod;
  impl_6 = new ClippedImageImpl();
}

function $ClippedImagePrototype(this$static, url, left, top, width, height){
  $clinit_214();
  this$static.url = url;
  this$static.left = left;
  this$static.top = top;
  this$static.width_0 = width;
  this$static.height_0 = height;
  return this$static;
}

function $applyTo(this$static, image){
  $setUrlAndVisibleRect(image, this$static.url, this$static.left, this$static.top, this$static.width_0, this$static.height_0);
}

function $createImage(this$static){
  return $Image_1(new Image_0(), this$static.url, this$static.left, this$static.top, this$static.width_0, this$static.height_0);
}

function $getHTML_1(this$static){
  return $getHTML_0(impl_6, this$static.url, this$static.left, this$static.top, this$static.width_0, this$static.height_0);
}

function ClippedImagePrototype(){
}

_ = ClippedImagePrototype.prototype = new AbstractImagePrototype();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'ClippedImagePrototype';
_.typeId$ = 168;
_.height_0 = 0;
_.left = 0;
_.top = 0;
_.url = null;
_.width_0 = 0;
var impl_6;
function $clinit_217(){
  $clinit_217 = nullMethod;
  implPanel = $FocusImplSafari(new FocusImplSafari());
  implWidget = implPanel !== null?$FocusImpl(new FocusImpl()):implPanel;
}

function $FocusImpl(this$static){
  $clinit_217();
  return this$static;
}

function FocusImpl(){
}

_ = FocusImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'FocusImpl';
_.typeId$ = 169;
var implPanel, implWidget;
function $clinit_215(){
  $clinit_215 = nullMethod;
  $clinit_217();
}

function $$init_31(this$static){
  this$static.blurHandler = $createBlurHandler(this$static);
  this$static.focusHandler = $createFocusHandler(this$static);
  this$static.mouseHandler = $createMouseHandler(this$static);
}

function $FocusImplOld(this$static){
  $clinit_215();
  $FocusImpl(this$static);
  $$init_31(this$static);
  return this$static;
}

function $createBlurHandler(this$static){
  return function(evt){
    if (this.parentNode.onblur) {
      this.parentNode.onblur(evt);
    }
  }
  ;
}

function $createFocusHandler(this$static){
  return function(evt){
    if (this.parentNode.onfocus) {
      this.parentNode.onfocus(evt);
    }
  }
  ;
}

function $createFocusable(this$static){
  var div = $doc.createElement('div');
  var input = this$static.createHiddenInput();
  input.addEventListener('blur', this$static.blurHandler, false);
  input.addEventListener('focus', this$static.focusHandler, false);
  div.addEventListener('mousedown', this$static.mouseHandler, false);
  div.appendChild(input);
  return div;
}

function createHiddenInput(){
  var input = $doc.createElement('input');
  input.type = 'text';
  input.style.width = input.style.height = 0;
  input.style.zIndex = -1;
  input.style.position = 'absolute';
  return input;
}

function FocusImplOld(){
}

_ = FocusImplOld.prototype = new FocusImpl();
_.createHiddenInput = createHiddenInput;
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'FocusImplOld';
_.typeId$ = 170;
function $clinit_216(){
  $clinit_216 = nullMethod;
  $clinit_215();
}

function $FocusImplSafari(this$static){
  $clinit_216();
  $FocusImplOld(this$static);
  return this$static;
}

function $blur_0(this$static, elem){
  $wnd.setTimeout(function(){
    elem.firstChild.blur();
  }
  , 0);
}

function $createMouseHandler(this$static){
  return function(){
    var firstChild = this.firstChild;
    $wnd.setTimeout(function(){
      firstChild.focus();
    }
    , 0);
  }
  ;
}

function $focus(this$static, elem){
  $wnd.setTimeout(function(){
    elem.firstChild.focus();
  }
  , 0);
}

function createHiddenInput_0(){
  var input = $doc.createElement('input');
  input.type = 'text';
  input.style.opacity = 0;
  input.style.zIndex = -1;
  input.style.height = '1px';
  input.style.width = '1px';
  input.style.overflow = 'hidden';
  input.style.position = 'absolute';
  return input;
}

function FocusImplSafari(){
}

_ = FocusImplSafari.prototype = new FocusImplOld();
_.createHiddenInput = createHiddenInput_0;
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'FocusImplSafari';
_.typeId$ = 171;
function $createElement_0(this$static){
  return createDiv();
}

function PopupImpl(){
}

_ = PopupImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'PopupImpl';
_.typeId$ = 172;
function $RichTextAreaImpl(this$static){
  this$static.elem = $createElement_1(this$static);
  return this$static;
}

function $onElementInitialized(this$static){
  this$static.hookEvents();
}

function RichTextAreaImpl(){
}

_ = RichTextAreaImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'RichTextAreaImpl';
_.typeId$ = 173;
_.elem = null;
function $$init_32(this$static){
  this$static.beforeInitPlaceholder = createDiv();
}

function $RichTextAreaImplStandard(this$static){
  $RichTextAreaImpl(this$static);
  $$init_32(this$static);
  return this$static;
}

function $createElement_2(this$static){
  return $doc.createElement('iframe');
}

function $createLink(this$static, url){
  $execCommand(this$static, 'CreateLink', url);
}

function $execCommand(this$static, cmd, param){
  if ($isRichEditingActive(this$static, this$static.elem)) {
    this$static.setFocus(true);
    $execCommandAssumingFocus(this$static, cmd, param);
  }
}

function $execCommandAssumingFocus(this$static, cmd, param){
  this$static.elem.contentWindow.document.execCommand(cmd, false, param);
}

function $getHTML_2(this$static){
  return this$static.beforeInitPlaceholder === null?$getHTMLImpl(this$static):getInnerHTML(this$static.beforeInitPlaceholder);
}

function $getHTMLImpl(this$static){
  return this$static.elem.contentWindow.document.body.innerHTML;
}

function $initElement(this$static){
  var _this = this$static;
  setTimeout(function(){
    _this.elem.contentWindow.document.designMode = 'On';
    _this.onElementInitialized();
  }
  , 1);
}

function $insertHorizontalRule(this$static){
  $execCommand(this$static, 'InsertHorizontalRule', null);
}

function $insertImage(this$static, url){
  $execCommand(this$static, 'InsertImage', url);
}

function $insertOrderedList(this$static){
  $execCommand(this$static, 'InsertOrderedList', null);
}

function $insertUnorderedList(this$static){
  $execCommand(this$static, 'InsertUnorderedList', null);
}

function $isRichEditingActive(this$static, e){
  return e.contentWindow.document.designMode.toUpperCase() == 'ON';
}

function $isStrikethrough(this$static){
  return $queryCommandState(this$static, 'Strikethrough');
}

function $isSubscript(this$static){
  return $queryCommandState(this$static, 'Subscript');
}

function $isSuperscript(this$static){
  return $queryCommandState(this$static, 'Superscript');
}

function $leftIndent(this$static){
  $execCommand(this$static, 'Outdent', null);
}

function $queryCommandState(this$static, cmd){
  if ($isRichEditingActive(this$static, this$static.elem)) {
    this$static.setFocus(true);
    return $queryCommandStateAssumingFocus(this$static, cmd);
  }
   else {
    return false;
  }
}

function $queryCommandStateAssumingFocus(this$static, cmd){
  return !(!this$static.elem.contentWindow.document.queryCommandState(cmd));
}

function $removeFormat(this$static){
  $execCommand(this$static, 'RemoveFormat', null);
}

function $removeLink(this$static){
  $execCommand(this$static, 'Unlink', 'false');
}

function $rightIndent(this$static){
  $execCommand(this$static, 'Indent', null);
}

function $setBackColor_0(this$static, color){
  $execCommand(this$static, 'BackColor', color);
}

function $setFontName(this$static, name){
  $execCommand(this$static, 'FontName', name);
}

function $setFontSize_0(this$static, fontSize){
  $execCommand(this$static, 'FontSize', toString_9(fontSize.number));
}

function $setForeColor(this$static, color){
  $execCommand(this$static, 'ForeColor', color);
}

function $setHTMLImpl(this$static, html){
  this$static.elem.contentWindow.document.body.innerHTML = html;
}

function $setJustification(this$static, justification){
  if (justification === ($clinit_157() , CENTER_0)) {
    $execCommand(this$static, 'JustifyCenter', null);
  }
   else if (justification === ($clinit_157() , LEFT)) {
    $execCommand(this$static, 'JustifyLeft', null);
  }
   else if (justification === ($clinit_157() , RIGHT)) {
    $execCommand(this$static, 'JustifyRight', null);
  }
}

function $toggleBold(this$static){
  $execCommand(this$static, 'Bold', 'false');
}

function $toggleItalic(this$static){
  $execCommand(this$static, 'Italic', 'false');
}

function $toggleStrikethrough(this$static){
  $execCommand(this$static, 'Strikethrough', 'false');
}

function $toggleSubscript(this$static){
  $execCommand(this$static, 'Subscript', 'false');
}

function $toggleSuperscript(this$static){
  $execCommand(this$static, 'Superscript', 'false');
}

function $toggleUnderline(this$static){
  $execCommand(this$static, 'Underline', 'False');
}

function $uninitElement(this$static){
  var html;
  $unhookEvents(this$static);
  html = $getHTML_2(this$static);
  this$static.beforeInitPlaceholder = createDiv();
  setInnerHTML(this$static.beforeInitPlaceholder, html);
}

function hookEvents_0(){
  var elem = this.elem;
  var wnd = elem.contentWindow;
  elem.__gwt_handler = function(evt){
    if (elem.__listener) {
      elem.__listener.onBrowserEvent(evt);
    }
  }
  ;
  elem.__gwt_focusHandler = function(evt){
    if (elem.__gwt_isFocused) {
      return;
    }
    elem.__gwt_isFocused = true;
    elem.__gwt_handler(evt);
  }
  ;
  elem.__gwt_blurHandler = function(evt){
    if (!elem.__gwt_isFocused) {
      return;
    }
    elem.__gwt_isFocused = false;
    elem.__gwt_handler(evt);
  }
  ;
  wnd.addEventListener('keydown', elem.__gwt_handler, true);
  wnd.addEventListener('keyup', elem.__gwt_handler, true);
  wnd.addEventListener('keypress', elem.__gwt_handler, true);
  wnd.addEventListener('mousedown', elem.__gwt_handler, true);
  wnd.addEventListener('mouseup', elem.__gwt_handler, true);
  wnd.addEventListener('mousemove', elem.__gwt_handler, true);
  wnd.addEventListener('mouseover', elem.__gwt_handler, true);
  wnd.addEventListener('mouseout', elem.__gwt_handler, true);
  wnd.addEventListener('click', elem.__gwt_handler, true);
  wnd.addEventListener('focus', elem.__gwt_focusHandler, true);
  wnd.addEventListener('blur', elem.__gwt_blurHandler, true);
}

function onElementInitialized(){
  $onElementInitialized(this);
  if (this.beforeInitPlaceholder !== null) {
    $setHTMLImpl(this, getInnerHTML(this.beforeInitPlaceholder));
    this.beforeInitPlaceholder = null;
  }
}

function RichTextAreaImplStandard(){
}

_ = RichTextAreaImplStandard.prototype = new RichTextAreaImpl();
_.hookEvents = hookEvents_0;
_.onElementInitialized = onElementInitialized;
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'RichTextAreaImplStandard';
_.typeId$ = 174;
function $clinit_219(){
  $clinit_219 = nullMethod;
  sizeNumberCSSValues = initValues('[Ljava.lang.String;', 205, 1, ['medium', 'xx-small', 'x-small', 'small', 'medium', 'large', 'x-large', 'xx-large']);
  webKitVersion = getWebKitVersion();
  extendedEditingSupported = webKitVersion >= 420;
  useHiliteColor = webKitVersion >= 420;
  oldSchoolSizeValues = webKitVersion <= 420;
}

function $RichTextAreaImplSafari(this$static){
  $clinit_219();
  $RichTextAreaImplStandard(this$static);
  return this$static;
}

function $createElement_1(this$static){
  return $createElement_2(this$static);
}

function $isBold(this$static){
  return !(!this$static.elem.__gwt_isBold);
}

function $isItalic(this$static){
  return !(!this$static.elem.__gwt_isItalic);
}

function $isUnderlined(this$static){
  return !(!this$static.elem.__gwt_isUnderlined);
}

function $setBackColor(this$static, color){
  if (useHiliteColor) {
    $execCommand(this$static, 'HiliteColor', color);
  }
   else {
    $setBackColor_0(this$static, color);
  }
}

function $setFocus_1(this$static, focused){
  var elem = this$static.elem;
  if (focused) {
    elem.focus();
    if (elem.__gwt_restoreSelection) {
      elem.__gwt_restoreSelection();
    }
  }
   else {
    elem.blur();
  }
}

function $setFontSize(this$static, fontSize){
  var number;
  if (oldSchoolSizeValues) {
    number = fontSize.number;
    if (number >= 0 && number <= 7) {
      $execCommand(this$static, 'FontSize', sizeNumberCSSValues[number]);
    }
  }
   else {
    $setFontSize_0(this$static, fontSize);
  }
}

function $unhookEvents(this$static){
  var elem = this$static.elem;
  var wnd = elem.contentWindow;
  wnd.removeEventListener('keydown', elem.__gwt_handler, true);
  wnd.removeEventListener('keyup', elem.__gwt_handler, true);
  wnd.removeEventListener('keypress', elem.__gwt_handler, true);
  wnd.removeEventListener('mousedown', elem.__gwt_handler, true);
  wnd.removeEventListener('mouseup', elem.__gwt_handler, true);
  wnd.removeEventListener('mousemove', elem.__gwt_handler, true);
  wnd.removeEventListener('mouseover', elem.__gwt_handler, true);
  wnd.removeEventListener('mouseout', elem.__gwt_handler, true);
  wnd.removeEventListener('click', elem.__gwt_handler, true);
  elem.__gwt_restoreSelection = null;
  elem.__gwt_handler = null;
  elem.onfocus = null;
  elem.onblur = null;
}

function getWebKitVersion(){
  $clinit_219();
  var exp = / AppleWebKit\/([\d]+)/;
  var result = exp.exec(navigator.userAgent);
  if (result) {
    var version = parseInt(result[1]);
    if (version) {
      return version;
    }
  }
  return 0;
}

function hookEvents(){
  var elem = this.elem;
  var wnd = elem.contentWindow;
  var doc = wnd.document;
  elem.__gwt_selection = {'baseOffset':0, 'extentOffset':0, 'baseNode':null, 'extentNode':null};
  elem.__gwt_restoreSelection = function(){
    var sel = elem.__gwt_selection;
    if (wnd.getSelection) {
      wnd.getSelection().setBaseAndExtent(sel.baseNode, sel.baseOffset, sel.extentNode, sel.extentOffset);
    }
  }
  ;
  elem.__gwt_handler = function(evt){
    var s = wnd.getSelection();
    elem.__gwt_selection = {'baseOffset':s.baseOffset, 'extentOffset':s.extentOffset, 'baseNode':s.baseNode, 'extentNode':s.extentNode};
    elem.__gwt_isBold = doc.queryCommandState('Bold');
    elem.__gwt_isItalic = doc.queryCommandState('Italic');
    elem.__gwt_isUnderlined = doc.queryCommandState('Underline');
    if (elem.__listener) {
      elem.__listener.onBrowserEvent(evt);
    }
  }
  ;
  wnd.addEventListener('keydown', elem.__gwt_handler, true);
  wnd.addEventListener('keyup', elem.__gwt_handler, true);
  wnd.addEventListener('keypress', elem.__gwt_handler, true);
  wnd.addEventListener('mousedown', elem.__gwt_handler, true);
  wnd.addEventListener('mouseup', elem.__gwt_handler, true);
  wnd.addEventListener('mousemove', elem.__gwt_handler, true);
  wnd.addEventListener('mouseover', elem.__gwt_handler, true);
  wnd.addEventListener('mouseout', elem.__gwt_handler, true);
  wnd.addEventListener('click', elem.__gwt_handler, true);
  elem.onfocus = function(evt){
    if (elem.__listener) {
      elem.__listener.onBrowserEvent(evt);
    }
  }
  ;
  elem.onblur = function(evt){
    if (elem.__listener) {
      elem.__listener.onBrowserEvent(evt);
    }
  }
  ;
}

function setFocus(focused){
  $setFocus_1(this, focused);
}

function RichTextAreaImplSafari(){
}

_ = RichTextAreaImplSafari.prototype = new RichTextAreaImplStandard();
_.hookEvents = hookEvents;
_.setFocus = setFocus;
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'RichTextAreaImplSafari';
_.typeId$ = 175;
var extendedEditingSupported, oldSchoolSizeValues, sizeNumberCSSValues, useHiliteColor, webKitVersion;
function $getCursorPos(this$static, elem){
  try {
    return elem.selectionStart;
  }
   catch (e) {
    return 0;
  }
}

function $getSelectionLength(this$static, elem){
  try {
    return elem.selectionEnd - elem.selectionStart;
  }
   catch (e) {
    return 0;
  }
}

function $getTextAreaCursorPos(this$static, elem){
  return $getCursorPos(this$static, elem);
}

function TextBoxImpl(){
}

_ = TextBoxImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'TextBoxImpl';
_.typeId$ = 176;
function ArrayStoreException(){
}

_ = ArrayStoreException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'ArrayStoreException';
_.typeId$ = 177;
function $clinit_224(){
  $clinit_224 = nullMethod;
  FALSE = $Boolean(new Boolean_0(), false);
  TRUE = $Boolean(new Boolean_0(), true);
}

function $Boolean(this$static, value){
  $clinit_224();
  this$static.value_0 = value;
  return this$static;
}

function equals_2(o){
  return instanceOf(o, 36) && dynamicCast(o, 36).value_0 == this.value_0;
}

function hashCode_3(){
  var hashCodeForFalse, hashCodeForTrue;
  hashCodeForTrue = 1231;
  hashCodeForFalse = 1237;
  return this.value_0?1231:1237;
}

function toString_8(){
  return this.value_0?'true':'false';
}

function valueOf(b){
  $clinit_224();
  return b?TRUE:FALSE;
}

function Boolean_0(){
}

_ = Boolean_0.prototype = new Object_0();
_.equals$ = equals_2;
_.hashCode$ = hashCode_3;
_.toString$ = toString_8;
_.typeName$ = package_java_lang_ + 'Boolean';
_.typeId$ = 178;
_.value_0 = false;
var FALSE, TRUE;
function $ClassCastException(this$static, message){
  $RuntimeException(this$static, message);
  return this$static;
}

function ClassCastException(){
}

_ = ClassCastException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'ClassCastException';
_.typeId$ = 179;
function $IllegalArgumentException(this$static, message){
  $RuntimeException(this$static, message);
  return this$static;
}

function IllegalArgumentException(){
}

_ = IllegalArgumentException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'IllegalArgumentException';
_.typeId$ = 180;
function $IllegalStateException(this$static, s){
  $RuntimeException(this$static, s);
  return this$static;
}

function IllegalStateException(){
}

_ = IllegalStateException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'IllegalStateException';
_.typeId$ = 181;
function $IndexOutOfBoundsException(this$static, message){
  $RuntimeException(this$static, message);
  return this$static;
}

function IndexOutOfBoundsException(){
}

_ = IndexOutOfBoundsException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'IndexOutOfBoundsException';
_.typeId$ = 182;
function $clinit_238(){
  $clinit_238 = nullMethod;
  __hexDigits = initValues('[Ljava.lang.String;', 205, 1, ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f']);
  {
    initNative();
  }
}

function initNative(){
  $clinit_238();
  floatRegex = /^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i;
}

var __hexDigits, floatRegex = null;
function $clinit_233(){
  $clinit_233 = nullMethod;
  $clinit_238();
}

function toString_9(b){
  $clinit_233();
  return valueOf_0(b);
}

var MAX_VALUE = 2147483647, MIN_VALUE = (-2147483648);
function $clinit_234(){
  $clinit_234 = nullMethod;
  $clinit_238();
}

function toHexString(x){
  $clinit_234();
  var hexStr, nibble;
  if (x == 0) {
    return '0';
  }
  hexStr = '';
  while (x != 0) {
    nibble = narrow_int(x) & 15;
    hexStr = __hexDigits[nibble] + hexStr;
    x = x >>> 4;
  }
  return hexStr;
}

function abs(x){
  return x < 0?-x:x;
}

function min(x, y){
  return x < y?x:y;
}

function NegativeArraySizeException(){
}

_ = NegativeArraySizeException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'NegativeArraySizeException';
_.typeId$ = 183;
function $NullPointerException(this$static, message){
  $RuntimeException(this$static, message);
  return this$static;
}

function NullPointerException(){
}

_ = NullPointerException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'NullPointerException';
_.typeId$ = 184;
function $charAt(this$static, index){
  return this$static.charCodeAt(index);
}

function $compareTo(this$static, other){
  var i, length, otherChar, otherLength, thisChar, thisLength;
  thisLength = $length(this$static);
  otherLength = $length(other);
  length = min(thisLength, otherLength);
  for (i = 0; i < length; i++) {
    thisChar = $charAt(this$static, i);
    otherChar = $charAt(other, i);
    if (thisChar != otherChar) {
      return thisChar - otherChar;
    }
  }
  return thisLength - otherLength;
}

function $equals_1(this$static, other){
  if (!instanceOf(other, 1))
    return false;
  return __equals(this$static, other);
}

function $indexOf_0(this$static, ch){
  return this$static.indexOf(String.fromCharCode(ch));
}

function $indexOf_1(this$static, str){
  return this$static.indexOf(str);
}

function $indexOf_2(this$static, str, startIndex){
  return this$static.indexOf(str, startIndex);
}

function $length(this$static){
  return this$static.length;
}

function $matches(this$static, regex){
  var matchObj = new RegExp(regex).exec(this$static);
  return matchObj == null?false:this$static == matchObj[0];
}

function $replace(this$static, from, to){
  var code = toHexString(from);
  return this$static.replace(RegExp('\\x' + code, 'g'), String.fromCharCode(to));
}

function $replaceAll(this$static, regex, replace){
  replace = __translateReplaceString(replace);
  return this$static.replace(RegExp(regex, 'g'), replace);
}

function $split(this$static, regex){
  return $split_0(this$static, regex, 0);
}

function $split_0(this$static, regex, maxMatch){
  var compiled = new RegExp(regex, 'g');
  var out = [];
  var count = 0;
  var trail = this$static;
  var lastTrail = null;
  while (true) {
    var matchObj = compiled.exec(trail);
    if (matchObj == null || (trail == '' || count == maxMatch - 1 && maxMatch > 0)) {
      out[count] = trail;
      break;
    }
     else {
      out[count] = trail.substring(0, matchObj.index);
      trail = trail.substring(matchObj.index + matchObj[0].length, trail.length);
      compiled.lastIndex = 0;
      if (lastTrail == trail) {
        out[count] = trail.substring(0, 1);
        trail = trail.substring(1);
      }
      lastTrail = trail;
      count++;
    }
  }
  if (maxMatch == 0) {
    for (var i = out.length - 1; i >= 0; i--) {
      if (out[i] != '') {
        out.splice(i + 1, out.length - (i + 1));
        break;
      }
    }
  }
  var jr = __createArray(out.length);
  var i = 0;
  for (i = 0; i < out.length; ++i) {
    jr[i] = out[i];
  }
  return jr;
}

function $substring(this$static, beginIndex){
  return this$static.substr(beginIndex, this$static.length - beginIndex);
}

function $substring_0(this$static, beginIndex, endIndex){
  return this$static.substr(beginIndex, endIndex - beginIndex);
}

function $toLowerCase(this$static){
  return this$static.toLowerCase();
}

function $trim(this$static){
  var r1 = this$static.replace(/^(\s*)/, '');
  var r2 = r1.replace(/\s*$/, '');
  return r2;
}

function __createArray(numElements){
  return initDims_0('[Ljava.lang.String;', [205], [1], [numElements], null);
}

function __equals(me, other){
  return String(me) == other;
}

function __translateReplaceString(replaceStr){
  var pos;
  pos = 0;
  while (0 <= (pos = $indexOf_2(replaceStr, '\\', pos))) {
    if ($charAt(replaceStr, pos + 1) == 36) {
      replaceStr = $substring_0(replaceStr, 0, pos) + '$' + $substring(replaceStr, ++pos);
    }
     else {
      replaceStr = $substring_0(replaceStr, 0, pos) + $substring(replaceStr, ++pos);
    }
  }
  return replaceStr;
}

function compareTo(other){
  if (instanceOf(other, 1)) {
    return $compareTo(this, dynamicCast(other, 1));
  }
   else {
    throw $ClassCastException(new ClassCastException(), 'Cannot compare ' + other + " with String '" + this + "'");
  }
}

function equals_4(other){
  return $equals_1(this, other);
}

function hashCode_5(){
  var hashCache = hashCache_0;
  if (!hashCache) {
    hashCache = hashCache_0 = {};
  }
  var key_0 = ':' + this;
  var hashCode = hashCache[key_0];
  if (hashCode == null) {
    hashCode = 0;
    var n = this.length;
    var inc = n < 64?1:n / 32 | 0;
    for (var i = 0; i < n; i += inc) {
      hashCode <<= 1;
      hashCode += this.charCodeAt(i);
    }
    hashCode |= 0;
    hashCache[key_0] = hashCode;
  }
  return hashCode;
}

function toString_12(){
  return this;
}

function valueOf_0(x){
  return '' + x;
}

function valueOf_1(x){
  return x !== null?x.toString$():'null';
}

_ = String.prototype;
_.compareTo = compareTo;
_.equals$ = equals_4;
_.hashCode$ = hashCode_5;
_.toString$ = toString_12;
_.typeName$ = package_java_lang_ + 'String';
_.typeId$ = 2;
var hashCache_0 = null;
function $StringBuffer(this$static){
  $assign(this$static);
  return this$static;
}

function $append(this$static, toAppend){
  if (toAppend === null) {
    toAppend = 'null';
  }
  var last = this$static.js.length - 1;
  var lastLength = this$static.js[last].length;
  if (this$static.length > lastLength * lastLength) {
    this$static.js[last] = this$static.js[last] + toAppend;
  }
   else {
    this$static.js.push(toAppend);
  }
  this$static.length += toAppend.length;
  return this$static;
}

function $assign(this$static){
  $assign_0(this$static, '');
}

function $assign_0(this$static, s){
  this$static.js = [s];
  this$static.length = s.length;
}

function $toString_0(this$static){
  this$static.normalize();
  return this$static.js[0];
}

function normalize(){
  if (this.js.length > 1) {
    this.js = [this.js.join('')];
    this.length = this.js[0].length;
  }
}

function toString_11(){
  return $toString_0(this);
}

function StringBuffer(){
}

_ = StringBuffer.prototype = new Object_0();
_.normalize = normalize;
_.toString$ = toString_11;
_.typeName$ = package_java_lang_ + 'StringBuffer';
_.typeId$ = 185;
function currentTimeMillis_0(){
  return new Date().getTime();
}

function identityHashCode(o){
  return getHashCode_0(o);
}

function $UnsupportedOperationException(this$static, message){
  $RuntimeException(this$static, message);
  return this$static;
}

function UnsupportedOperationException(){
}

_ = UnsupportedOperationException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'UnsupportedOperationException';
_.typeId$ = 186;
function $AbstractList$IteratorImpl(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $hasNext_4(this$static){
  return this$static.i < this$static.this$0.size_0();
}

function $next_3(this$static){
  if (!$hasNext_4(this$static)) {
    throw new NoSuchElementException();
  }
  return this$static.this$0.get(this$static.last = this$static.i++);
}

function $remove_10(this$static){
  if (this$static.last < 0) {
    throw new IllegalStateException();
  }
  this$static.this$0.remove(this$static.last);
  this$static.i = this$static.last;
  this$static.last = (-1);
}

function hasNext_5(){
  return $hasNext_4(this);
}

function next_6(){
  return $next_3(this);
}

function AbstractList$IteratorImpl(){
}

_ = AbstractList$IteratorImpl.prototype = new Object_0();
_.hasNext = hasNext_5;
_.next_0 = next_6;
_.typeName$ = package_java_util_ + 'AbstractList$IteratorImpl';
_.typeId$ = 187;
_.i = 0;
_.last = (-1);
function $implFindEntry(this$static, key_0, remove){
  var entry, iter, k;
  for (iter = $iterator_6(this$static.entrySet()); $hasNext_7(iter);) {
    entry = $next_6(iter);
    k = entry.getKey();
    if (key_0 === null?k === null:key_0.equals$(k)) {
      if (remove) {
        $remove_14(iter);
      }
      return entry;
    }
  }
  return null;
}

function $keySet(this$static){
  var entrySet;
  entrySet = this$static.entrySet();
  return $AbstractMap$1(new AbstractMap$1(), this$static, entrySet);
}

function $values(this$static){
  var entrySet;
  entrySet = $entrySet(this$static);
  return $AbstractMap$3(new AbstractMap$3(), this$static, entrySet);
}

function containsKey(key_0){
  return $implFindEntry(this, key_0, false) !== null;
}

function equals_6(obj){
  var iter, key_0, keys, otherKeys, otherMap, otherValue, value;
  if (obj === this) {
    return true;
  }
  if (!instanceOf(obj, 43)) {
    return false;
  }
  otherMap = dynamicCast(obj, 43);
  keys = $keySet(this);
  otherKeys = otherMap.keySet();
  if (!$equals_2(keys, otherKeys)) {
    return false;
  }
  for (iter = $iterator_4(keys); $hasNext_5(iter);) {
    key_0 = $next_4(iter);
    value = this.get_0(key_0);
    otherValue = otherMap.get_0(key_0);
    if (value === null?otherValue !== null:!value.equals$(otherValue)) {
      return false;
    }
  }
  return true;
}

function get_1(key_0){
  var entry;
  entry = $implFindEntry(this, key_0, false);
  return entry === null?null:entry.getValue();
}

function hashCode_7(){
  var entry, hashCode, iter;
  hashCode = 0;
  for (iter = $iterator_6(this.entrySet()); $hasNext_7(iter);) {
    entry = $next_6(iter);
    hashCode += entry.hashCode$();
  }
  return hashCode;
}

function keySet(){
  return $keySet(this);
}

function toString_15(){
  var comma, entry, iter, s;
  s = '{';
  comma = false;
  for (iter = $iterator_6(this.entrySet()); $hasNext_7(iter);) {
    entry = $next_6(iter);
    if (comma) {
      s += ', ';
    }
     else {
      comma = true;
    }
    s += valueOf_1(entry.getKey());
    s += '=';
    s += valueOf_1(entry.getValue());
  }
  return s + '}';
}

function AbstractMap(){
}

_ = AbstractMap.prototype = new Object_0();
_.containsKey = containsKey;
_.equals$ = equals_6;
_.get_0 = get_1;
_.hashCode$ = hashCode_7;
_.keySet = keySet;
_.toString$ = toString_15;
_.typeName$ = package_java_util_ + 'AbstractMap';
_.typeId$ = 188;
function $equals_2(this$static, o){
  var iter, other, otherItem;
  if (o === this$static) {
    return true;
  }
  if (!instanceOf(o, 44)) {
    return false;
  }
  other = dynamicCast(o, 44);
  if (other.size_0() != this$static.size_0()) {
    return false;
  }
  for (iter = other.iterator_0(); iter.hasNext();) {
    otherItem = iter.next_0();
    if (!this$static.contains(otherItem)) {
      return false;
    }
  }
  return true;
}

function equals_7(o){
  return $equals_2(this, o);
}

function hashCode_8(){
  var hashCode, iter, next;
  hashCode = 0;
  for (iter = this.iterator_0(); iter.hasNext();) {
    next = iter.next_0();
    if (next !== null) {
      hashCode += next.hashCode$();
    }
  }
  return hashCode;
}

function AbstractSet(){
}

_ = AbstractSet.prototype = new AbstractCollection();
_.equals$ = equals_7;
_.hashCode$ = hashCode_8;
_.typeName$ = package_java_util_ + 'AbstractSet';
_.typeId$ = 189;
function $AbstractMap$1(this$static, this$0, val$entrySet){
  this$static.this$0 = this$0;
  this$static.val$entrySet = val$entrySet;
  return this$static;
}

function $iterator_4(this$static){
  var outerIter;
  outerIter = $iterator_6(this$static.val$entrySet);
  return $AbstractMap$2(new AbstractMap$2(), this$static, outerIter);
}

function contains_1(key_0){
  return this.this$0.containsKey(key_0);
}

function iterator_8(){
  return $iterator_4(this);
}

function size_1(){
  return this.val$entrySet.this$0.size;
}

function AbstractMap$1(){
}

_ = AbstractMap$1.prototype = new AbstractSet();
_.contains = contains_1;
_.iterator_0 = iterator_8;
_.size_0 = size_1;
_.typeName$ = package_java_util_ + 'AbstractMap$1';
_.typeId$ = 190;
function $AbstractMap$2(this$static, this$1, val$outerIter){
  this$static.val$outerIter = val$outerIter;
  return this$static;
}

function $hasNext_5(this$static){
  return $hasNext_7(this$static.val$outerIter);
}

function $next_4(this$static){
  var entry;
  entry = $next_6(this$static.val$outerIter);
  return entry.getKey();
}

function $remove_11(this$static){
  $remove_14(this$static.val$outerIter);
}

function hasNext_6(){
  return $hasNext_5(this);
}

function next_7(){
  return $next_4(this);
}

function AbstractMap$2(){
}

_ = AbstractMap$2.prototype = new Object_0();
_.hasNext = hasNext_6;
_.next_0 = next_7;
_.typeName$ = package_java_util_ + 'AbstractMap$2';
_.typeId$ = 191;
function $AbstractMap$3(this$static, this$0, val$entrySet){
  this$static.this$0 = this$0;
  this$static.val$entrySet = val$entrySet;
  return this$static;
}

function $iterator_5(this$static){
  var outerIter;
  outerIter = $iterator_6(this$static.val$entrySet);
  return $AbstractMap$4(new AbstractMap$4(), this$static, outerIter);
}

function contains_2(value){
  return $containsValue(this.this$0, value);
}

function iterator_9(){
  return $iterator_5(this);
}

function size_2(){
  return this.val$entrySet.this$0.size;
}

function AbstractMap$3(){
}

_ = AbstractMap$3.prototype = new AbstractCollection();
_.contains = contains_2;
_.iterator_0 = iterator_9;
_.size_0 = size_2;
_.typeName$ = package_java_util_ + 'AbstractMap$3';
_.typeId$ = 192;
function $AbstractMap$4(this$static, this$1, val$outerIter){
  this$static.val$outerIter = val$outerIter;
  return this$static;
}

function $hasNext_6(this$static){
  return $hasNext_7(this$static.val$outerIter);
}

function $next_5(this$static){
  var value;
  value = $next_6(this$static.val$outerIter).getValue();
  return value;
}

function hasNext_7(){
  return $hasNext_6(this);
}

function next_8(){
  return $next_5(this);
}

function AbstractMap$4(){
}

_ = AbstractMap$4.prototype = new Object_0();
_.hasNext = hasNext_7;
_.next_0 = next_8;
_.typeName$ = package_java_util_ + 'AbstractMap$4';
_.typeId$ = 193;
function nativeSort(array, size, compare){
  if (size == 0) {
    return;
  }
  var v = new Array();
  for (var i = 0; i < size; ++i) {
    v[i] = array[i];
  }
  if (compare != null) {
    var f = function(a, b){
      var c = compare.compare(a, b);
      return c;
    }
    ;
    v.sort(f);
  }
   else {
    v.sort();
  }
  for (i = 0; i < size; ++i) {
    array[i] = v[i];
  }
}

function sort(x){
  nativeSort(x, x.length_0, ($clinit_261() , NATURAL));
}

function $clinit_258(){
  $clinit_258 = nullMethod;
  $HashSet(new HashSet());
  $HashMap(new HashMap());
  $ArrayList(new ArrayList());
}

function replaceContents(target, x){
  $clinit_258();
  var i, size;
  size = target.size;
  for (i = 0; i < size; i++) {
    $set(target, i, x[i]);
  }
}

function sort_0(target){
  $clinit_258();
  var x;
  x = target.toArray();
  sort(x);
  replaceContents(target, x);
}

function $clinit_261(){
  $clinit_261 = nullMethod;
  NATURAL = new Comparators$1();
}

var NATURAL;
function compare_1(o1, o2){
  return dynamicCast(o1, 40).compareTo(o2);
}

function Comparators$1(){
}

_ = Comparators$1.prototype = new Object_0();
_.compare = compare_1;
_.typeName$ = package_java_util_ + 'Comparators$1';
_.typeId$ = 194;
function $clinit_267(){
  $clinit_267 = nullMethod;
  UNDEFINED = createUndefinedValue();
}

function $$init_34(this$static){
  {
    $clearImpl_0(this$static);
  }
}

function $HashMap(this$static){
  $clinit_267();
  $$init_34(this$static);
  return this$static;
}

function $clearImpl_0(this$static){
  this$static.hashCodeMap = createArray();
  this$static.stringMap = createObject();
  this$static.nullSlot = wrapJSO(UNDEFINED, JavaScriptObject);
  this$static.size = 0;
}

function $containsKey(this$static, key_0){
  if (instanceOf(key_0, 1)) {
    return getStringValue(this$static.stringMap, dynamicCast(key_0, 1)) !== UNDEFINED;
  }
   else if (key_0 === null) {
    return this$static.nullSlot !== UNDEFINED;
  }
   else {
    return getHashValue(this$static.hashCodeMap, key_0, key_0.hashCode$()) !== UNDEFINED;
  }
}

function $containsValue(this$static, value){
  if (this$static.nullSlot !== UNDEFINED && equalsWithNullCheck(this$static.nullSlot, value)) {
    return true;
  }
   else if (containsStringValue(this$static.stringMap, value)) {
    return true;
  }
   else if (containsHashValue(this$static.hashCodeMap, value)) {
    return true;
  }
  return false;
}

function $entrySet(this$static){
  return $HashMap$EntrySet(new HashMap$EntrySet(), this$static);
}

function $get_1(this$static, key_0){
  var result;
  if (instanceOf(key_0, 1)) {
    result = getStringValue(this$static.stringMap, dynamicCast(key_0, 1));
  }
   else if (key_0 === null) {
    result = this$static.nullSlot;
  }
   else {
    result = getHashValue(this$static.hashCodeMap, key_0, key_0.hashCode$());
  }
  return result === UNDEFINED?null:result;
}

function $put(this$static, key_0, value){
  var previous;
  if (instanceOf(key_0, 1)) {
    previous = putStringValue(this$static.stringMap, dynamicCast(key_0, 1), value);
  }
   else if (key_0 === null) {
    previous = this$static.nullSlot;
    this$static.nullSlot = value;
  }
   else {
    previous = putHashValue(this$static.hashCodeMap, key_0, value, key_0.hashCode$());
  }
  if (previous === UNDEFINED) {
    ++this$static.size;
    return null;
  }
   else {
    return previous;
  }
}

function $remove_15(this$static, key_0){
  var previous;
  if (instanceOf(key_0, 1)) {
    previous = removeStringValue(this$static.stringMap, dynamicCast(key_0, 1));
  }
   else if (key_0 === null) {
    previous = this$static.nullSlot;
    this$static.nullSlot = wrapJSO(UNDEFINED, JavaScriptObject);
  }
   else {
    previous = removeHashValue(this$static.hashCodeMap, key_0, key_0.hashCode$());
  }
  if (previous === UNDEFINED) {
    return null;
  }
   else {
    --this$static.size;
    return previous;
  }
}

function addAllHashEntries(hashCodeMap, dest){
  $clinit_267();
  for (var hashCode in hashCodeMap) {
    if (hashCode == parseInt(hashCode)) {
      var array = hashCodeMap[hashCode];
      for (var i = 0, c = array.length; i < c; ++i) {
        dest.add_0(array[i]);
      }
    }
  }
}

function addAllStringEntries(stringMap, dest){
  $clinit_267();
  for (var key_0 in stringMap) {
    if (key_0.charCodeAt(0) == 58) {
      var value = stringMap[key_0];
      var entry = create(key_0.substring(1), value);
      dest.add_0(entry);
    }
  }
}

function containsHashValue(hashCodeMap, value){
  $clinit_267();
  for (var hashCode in hashCodeMap) {
    if (hashCode == parseInt(hashCode)) {
      var array = hashCodeMap[hashCode];
      for (var i = 0, c = array.length; i < c; ++i) {
        var entry = array[i];
        var entryValue = entry.getValue();
        if (equalsWithNullCheck(value, entryValue)) {
          return true;
        }
      }
    }
  }
  return false;
}

function containsKey_0(key_0){
  return $containsKey(this, key_0);
}

function containsStringValue(stringMap, value){
  $clinit_267();
  for (var key_0 in stringMap) {
    if (key_0.charCodeAt(0) == 58) {
      var entryValue = stringMap[key_0];
      if (equalsWithNullCheck(value, entryValue)) {
        return true;
      }
    }
  }
  return false;
}

function createUndefinedValue(){
  $clinit_267();
}

function entrySet_0(){
  return $entrySet(this);
}

function equalsWithNullCheck(a, b){
  $clinit_267();
  if (a === b) {
    return true;
  }
   else if (a === null) {
    return false;
  }
   else {
    return a.equals$(b);
  }
}

function get_3(key_0){
  return $get_1(this, key_0);
}

function getHashValue(hashCodeMap, key_0, hashCode){
  $clinit_267();
  var array = hashCodeMap[hashCode];
  if (array) {
    for (var i = 0, c = array.length; i < c; ++i) {
      var entry = array[i];
      var entryKey = entry.getKey();
      if (equalsWithNullCheck(key_0, entryKey)) {
        return entry.getValue();
      }
    }
  }
}

function getStringValue(stringMap, key_0){
  $clinit_267();
  return stringMap[':' + key_0];
}

function putHashValue(hashCodeMap, key_0, value, hashCode){
  $clinit_267();
  var array = hashCodeMap[hashCode];
  if (array) {
    for (var i = 0, c = array.length; i < c; ++i) {
      var entry = array[i];
      var entryKey = entry.getKey();
      if (equalsWithNullCheck(key_0, entryKey)) {
        var previous = entry.getValue();
        entry.setValue(value);
        return previous;
      }
    }
  }
   else {
    array = hashCodeMap[hashCode] = [];
  }
  var entry = create(key_0, value);
  array.push(entry);
}

function putStringValue(stringMap, key_0, value){
  $clinit_267();
  key_0 = ':' + key_0;
  var result = stringMap[key_0];
  stringMap[key_0] = value;
  return result;
}

function removeHashValue(hashCodeMap, key_0, hashCode){
  $clinit_267();
  var array = hashCodeMap[hashCode];
  if (array) {
    for (var i = 0, c = array.length; i < c; ++i) {
      var entry = array[i];
      var entryKey = entry.getKey();
      if (equalsWithNullCheck(key_0, entryKey)) {
        if (array.length == 1) {
          delete hashCodeMap[hashCode];
        }
         else {
          array.splice(i, 1);
        }
        return entry.getValue();
      }
    }
  }
}

function removeStringValue(stringMap, key_0){
  $clinit_267();
  key_0 = ':' + key_0;
  var result = stringMap[key_0];
  delete stringMap[key_0];
  return result;
}

function HashMap(){
}

_ = HashMap.prototype = new AbstractMap();
_.containsKey = containsKey_0;
_.entrySet = entrySet_0;
_.get_0 = get_3;
_.typeName$ = package_java_util_ + 'HashMap';
_.typeId$ = 195;
_.hashCodeMap = null;
_.nullSlot = null;
_.size = 0;
_.stringMap = null;
var UNDEFINED;
function $HashMap$EntryImpl(this$static, key_0, value){
  this$static.key_0 = key_0;
  this$static.value_0 = value;
  return this$static;
}

function create(key_0, value){
  return $HashMap$EntryImpl(new HashMap$EntryImpl(), key_0, value);
}

function equals_9(other){
  var entry;
  if (instanceOf(other, 45)) {
    entry = dynamicCast(other, 45);
    if (equalsWithNullCheck(this.key_0, entry.getKey()) && equalsWithNullCheck(this.value_0, entry.getValue())) {
      return true;
    }
  }
  return false;
}

function getKey(){
  return this.key_0;
}

function getValue_0(){
  return this.value_0;
}

function hashCode_9(){
  var keyHash, valueHash;
  keyHash = 0;
  valueHash = 0;
  if (this.key_0 !== null) {
    keyHash = this.key_0.hashCode$();
  }
  if (this.value_0 !== null) {
    valueHash = this.value_0.hashCode$();
  }
  return keyHash ^ valueHash;
}

function setValue(object){
  var old;
  old = this.value_0;
  this.value_0 = object;
  return old;
}

function toString_16(){
  return this.key_0 + '=' + this.value_0;
}

function HashMap$EntryImpl(){
}

_ = HashMap$EntryImpl.prototype = new Object_0();
_.equals$ = equals_9;
_.getKey = getKey;
_.getValue = getValue_0;
_.hashCode$ = hashCode_9;
_.setValue = setValue;
_.toString$ = toString_16;
_.typeName$ = package_java_util_ + 'HashMap$EntryImpl';
_.typeId$ = 196;
_.key_0 = null;
_.value_0 = null;
function $HashMap$EntrySet(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $iterator_6(this$static){
  return $HashMap$EntrySetIterator(new HashMap$EntrySetIterator(), this$static.this$0);
}

function contains_4(o){
  var entry, key_0, value;
  if (instanceOf(o, 45)) {
    entry = dynamicCast(o, 45);
    key_0 = entry.getKey();
    if ($containsKey(this.this$0, key_0)) {
      value = $get_1(this.this$0, key_0);
      return equalsWithNullCheck(entry.getValue(), value);
    }
  }
  return false;
}

function iterator_10(){
  return $iterator_6(this);
}

function size_4(){
  return this.this$0.size;
}

function HashMap$EntrySet(){
}

_ = HashMap$EntrySet.prototype = new AbstractSet();
_.contains = contains_4;
_.iterator_0 = iterator_10;
_.size_0 = size_4;
_.typeName$ = package_java_util_ + 'HashMap$EntrySet';
_.typeId$ = 197;
function $HashMap$EntrySetIterator(this$static, this$0){
  var list;
  this$static.this$0 = this$0;
  list = $ArrayList(new ArrayList());
  if (this$static.this$0.nullSlot !== ($clinit_267() , UNDEFINED)) {
    $add_9(list, $HashMap$EntryImpl(new HashMap$EntryImpl(), null, this$static.this$0.nullSlot));
  }
  addAllStringEntries(this$static.this$0.stringMap, list);
  addAllHashEntries(this$static.this$0.hashCodeMap, list);
  this$static.iter = $iterator_3(list);
  return this$static;
}

function $hasNext_7(this$static){
  return $hasNext_4(this$static.iter);
}

function $next_6(this$static){
  return this$static.last = dynamicCast($next_3(this$static.iter), 45);
}

function $remove_14(this$static){
  if (this$static.last === null) {
    throw $IllegalStateException(new IllegalStateException(), 'Must call next() before remove().');
  }
   else {
    $remove_10(this$static.iter);
    $remove_15(this$static.this$0, this$static.last.getKey());
    this$static.last = null;
  }
}

function hasNext_8(){
  return $hasNext_7(this);
}

function next_9(){
  return $next_6(this);
}

function HashMap$EntrySetIterator(){
}

_ = HashMap$EntrySetIterator.prototype = new Object_0();
_.hasNext = hasNext_8;
_.next_0 = next_9;
_.typeName$ = package_java_util_ + 'HashMap$EntrySetIterator';
_.typeId$ = 198;
_.iter = null;
_.last = null;
function $HashSet(this$static){
  this$static.map = $HashMap(new HashMap());
  return this$static;
}

function $add_10(this$static, o){
  var old;
  old = $put(this$static.map, o, valueOf(true));
  return old === null;
}

function $contains_1(this$static, o){
  return $containsKey(this$static.map, o);
}

function $iterator_7(this$static){
  return $iterator_4($keySet(this$static.map));
}

function add_7(o){
  return $add_10(this, o);
}

function contains_5(o){
  return $contains_1(this, o);
}

function iterator_11(){
  return $iterator_7(this);
}

function size_5(){
  return this.map.size;
}

function toString_17(){
  return $keySet(this.map).toString$();
}

function HashSet(){
}

_ = HashSet.prototype = new AbstractSet();
_.add_0 = add_7;
_.contains = contains_5;
_.iterator_0 = iterator_11;
_.size_0 = size_5;
_.toString$ = toString_17;
_.typeName$ = package_java_util_ + 'HashSet';
_.typeId$ = 199;
_.map = null;
function $NoSuchElementException(this$static, s){
  $RuntimeException(this$static, s);
  return this$static;
}

function NoSuchElementException(){
}

_ = NoSuchElementException.prototype = new RuntimeException();
_.typeName$ = package_java_util_ + 'NoSuchElementException';
_.typeId$ = 200;
function init_6(){
  $onModuleLoad($KitchenSink(new KitchenSink()));
}

function gwtOnLoad(errFn, modName, modBase){
  $moduleName = modName;
  $moduleBase = modBase;
  if (errFn)
    try {
      init_6();
    }
     catch (e) {
      errFn(modName);
    }
   else {
    init_6();
  }
}

var typeIdArray = [{}, {23:1}, {1:1, 23:1, 40:1, 41:1}, {3:1, 23:1}, {3:1, 23:1}, {3:1, 23:1}, {3:1, 23:1}, {2:1, 23:1}, {23:1}, {23:1}, {23:1}, {23:1, 25:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {5:1, 23:1}, {5:1, 23:1}, {9:1, 23:1}, {12:1, 15:1, 23:1, 25:1, 26:1, 34:1}, {5:1, 23:1}, {23:1, 25:1, 33:1}, {4:1, 23:1, 25:1, 33:1}, {23:1, 38:1}, {15:1, 23:1, 25:1, 26:1}, {5:1, 23:1}, {13:1, 15:1, 23:1, 25:1, 26:1}, {5:1, 23:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {7:1, 15:1, 23:1, 25:1, 26:1, 35:1}, {7:1, 15:1, 18:1, 23:1, 25:1, 26:1, 35:1}, {7:1, 13:1, 15:1, 18:1, 23:1, 25:1, 26:1, 35:1}, {7:1, 15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1}, {12:1, 13:1, 16:1, 23:1}, {23:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {23:1}, {15:1, 23:1, 25:1, 26:1}, {5:1, 23:1}, {16:1, 23:1}, {16:1, 23:1}, {13:1, 23:1}, {6:1, 15:1, 23:1, 25:1, 26:1}, {5:1, 23:1}, {3:1, 23:1}, {23:1}, {10:1, 23:1}, {10:1, 23:1}, {10:1, 23:1}, {23:1}, {2:1, 8:1, 23:1}, {2:1, 23:1}, {11:1, 23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {23:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {20:1, 23:1}, {20:1, 23:1, 42:1}, {20:1, 23:1, 42:1}, {20:1, 23:1, 42:1}, {15:1, 23:1, 25:1, 26:1}, {20:1, 23:1, 42:1}, {15:1, 23:1, 25:1, 26:1}, {23:1}, {23:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {23:1}, {23:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {14:1, 15:1, 23:1, 25:1, 26:1}, {23:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {23:1}, {23:1}, {23:1, 37:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {23:1}, {23:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {6:1, 23:1}, {23:1}, {23:1}, {23:1}, {15:1, 23:1, 25:1, 26:1}, {6:1, 23:1}, {23:1}, {23:1}, {23:1}, {20:1, 23:1, 42:1}, {15:1, 23:1, 25:1, 26:1}, {23:1}, {23:1}, {15:1, 21:1, 23:1, 25:1, 26:1}, {7:1, 15:1, 23:1, 25:1, 26:1, 35:1}, {17:1, 23:1, 25:1}, {20:1, 23:1, 42:1}, {23:1}, {23:1}, {23:1, 30:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {20:1, 23:1, 42:1}, {20:1, 23:1}, {23:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {23:1, 39:1}, {23:1}, {15:1, 22:1, 23:1, 25:1, 26:1, 35:1}, {11:1, 23:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {23:1}, {15:1, 23:1, 25:1, 26:1}, {23:1}, {23:1}, {6:1, 23:1}, {16:1, 23:1}, {15:1, 21:1, 23:1, 25:1, 26:1}, {17:1, 23:1, 25:1, 31:1}, {7:1, 15:1, 23:1, 25:1, 26:1, 35:1}, {23:1}, {23:1}, {13:1, 15:1, 23:1, 25:1, 26:1}, {20:1, 23:1, 42:1}, {15:1, 23:1, 25:1, 26:1, 32:1, 35:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {13:1, 15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {23:1, 25:1, 33:1}, {20:1, 23:1, 42:1}, {15:1, 23:1, 25:1, 26:1, 35:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {23:1}, {3:1, 23:1}, {23:1, 36:1}, {3:1, 23:1}, {3:1, 23:1}, {3:1, 23:1}, {3:1, 23:1}, {3:1, 23:1}, {3:1, 23:1}, {23:1, 41:1}, {3:1, 23:1}, {23:1}, {23:1, 43:1}, {20:1, 23:1, 44:1}, {20:1, 23:1, 44:1}, {23:1}, {20:1, 23:1}, {23:1}, {23:1}, {23:1, 43:1}, {23:1, 45:1}, {20:1, 23:1, 44:1}, {23:1}, {19:1, 20:1, 23:1, 44:1}, {3:1, 23:1}, {23:1, 27:1}, {23:1}, {23:1, 27:1}, {23:1, 27:1}, {23:1, 24:1, 27:1, 28:1, 29:1}, {23:1, 27:1}, {23:1, 27:1}, {23:1, 27:1}, {23:1, 27:1}, {23:1, 27:1, 28:1}, {23:1, 27:1, 29:1}, {23:1, 27:1}, {23:1, 27:1}, {23:1, 27:1}, {23:1, 27:1}, {23:1, 27:1}, {23:1, 27:1}];

if (com_google_gwt_sample_kitchensink_KitchenSink) {
  var __gwt_initHandlers = com_google_gwt_sample_kitchensink_KitchenSink.__gwt_initHandlers;  com_google_gwt_sample_kitchensink_KitchenSink.onScriptLoad(gwtOnLoad);
}
})();
