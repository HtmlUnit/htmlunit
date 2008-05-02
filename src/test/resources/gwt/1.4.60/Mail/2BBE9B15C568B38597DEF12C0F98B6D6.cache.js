(function(){
var $wnd = window;
var $doc = $wnd.document;
var $moduleName, $moduleBase;
var _, package_com_google_gwt_core_client_ = 'com.google.gwt.core.client.', package_com_google_gwt_lang_ = 'com.google.gwt.lang.', package_com_google_gwt_sample_mail_client_ = 'com.google.gwt.sample.mail.client.', package_com_google_gwt_user_client_ = 'com.google.gwt.user.client.', package_com_google_gwt_user_client_impl_ = 'com.google.gwt.user.client.impl.', package_com_google_gwt_user_client_ui_ = 'com.google.gwt.user.client.ui.', package_com_google_gwt_user_client_ui_impl_ = 'com.google.gwt.user.client.ui.impl.', package_java_lang_ = 'java.lang.', package_java_util_ = 'java.util.';
function nullMethod(){
}

function equals_2(other){
  return this === other;
}

function hashCode_3(){
  return identityHashCode(this);
}

function Object_0(){
}

_ = Object_0.prototype = {};
_.equals$ = equals_2;
_.hashCode$ = hashCode_3;
_.typeName$ = package_java_lang_ + 'Object';
_.typeId$ = 1;
function getHostPageBaseURL(){
  return getHostPageBaseURL_0();
}

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

function getHostPageBaseURL_0(){
  var s = $doc.location.href;
  var i = s.indexOf('#');
  if (i != -1)
    s = s.substring(0, i);
  i = s.indexOf('?');
  if (i != -1)
    s = s.substring(0, i);
  i = s.lastIndexOf('/');
  if (i != -1)
    s = s.substring(0, i);
  return s.length > 0?s + '/':'';
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

function JavaScriptObject(){
}

_ = JavaScriptObject.prototype = new Object_0();
_.equals$ = equals;
_.hashCode$ = hashCode_0;
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
  if (x > ($clinit_147() , MAX_VALUE))
    return $clinit_147() , MAX_VALUE;
  if (x < ($clinit_147() , MIN_VALUE))
    return $clinit_147() , MIN_VALUE;
  return x >= 0?Math.floor(x):Math.ceil(x);
}

function throwClassCastException(){
  throw new ClassCastException();
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
function $addStyleName_0(this$static, style){
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

function $setStyleName_0(this$static, style){
  setStyleName(this$static.getStyleElement(), style);
}

function $sinkEvents_0(this$static, eventBitsToAdd){
  sinkEvents(this$static.getElement(), eventBitsToAdd | getEventsSunk(this$static.getElement()));
}

function getElement_0(){
  return this.element;
}

function getStyleElement_0(){
  return this.element;
}

function getStyleName(elem){
  return getElementProperty(elem, 'className');
}

function setElement_0(elem){
  $setElement_0(this, elem);
}

function setHeight_0(height){
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
  idx = $indexOf_0(oldStyle, style);
  while (idx != (-1)) {
    if (idx == 0 || $charAt(oldStyle, idx - 1) == 32) {
      last = idx + $length(style);
      lastPos = $length(oldStyle);
      if (last == lastPos || last < lastPos && $charAt(oldStyle, last) == 32) {
        break;
      }
    }
    idx = $indexOf_1(oldStyle, style, idx + 1);
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

function setVisible_0(elem, visible){
  elem.style.display = visible?'':'none';
}

function setVisible_1(visible){
  setVisible_0(this.element, visible);
}

function setWidth_1(width){
  setStyleAttribute(this.element, 'width', width);
}

function UIObject(){
}

_ = UIObject.prototype = new Object_0();
_.getElement = getElement_0;
_.getStyleElement = getStyleElement_0;
_.setElement = setElement_0;
_.setHeight = setHeight_0;
_.setVisible = setVisible_1;
_.setWidth = setWidth_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'UIObject';
_.typeId$ = 11;
_.element = null;
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
  if (instanceOf(this$static.parent, 17)) {
    dynamicCast(this$static.parent, 17).remove_0(this$static);
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

function onAttach_0(){
  if (this.isAttached()) {
    throw $IllegalStateException(new IllegalStateException(), "Should only call onAttach when the widget is detached from the browser's document");
  }
  this.attached = true;
  setEventListener(this.getElement(), this);
  this.doAttachChildren();
  this.onLoad();
}

function onBrowserEvent_7(event_0){
}

function onDetach_1(){
  $onDetach(this);
}

function onLoad_3(){
}

function onUnload_1(){
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
_.onAttach = onAttach_0;
_.onBrowserEvent = onBrowserEvent_7;
_.onDetach = onDetach_1;
_.onLoad = onLoad_3;
_.onUnload = onUnload_1;
_.setElement = setElement_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Widget';
_.typeId$ = 12;
_.attached = false;
_.layoutData = null;
_.parent = null;
function $adopt(this$static, child){
  $setParent(child, this$static);
}

function $orphan(this$static, child){
  $setParent(child, null);
}

function doAttachChildren(){
  var child, it;
  for (it = this.iterator_0(); it.hasNext();) {
    child = dynamicCast(it.next_0(), 12);
    child.onAttach();
  }
}

function doDetachChildren(){
  var child, it;
  for (it = this.iterator_0(); it.hasNext();) {
    child = dynamicCast(it.next_0(), 12);
    child.onDetach();
  }
}

function onLoad_1(){
}

function onUnload_0(){
}

function Panel(){
}

_ = Panel.prototype = new Widget();
_.doAttachChildren = doAttachChildren;
_.doDetachChildren = doDetachChildren;
_.onLoad = onLoad_1;
_.onUnload = onUnload_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Panel';
_.typeId$ = 13;
function $SimplePanel(this$static){
  $SimplePanel_0(this$static, createDiv());
  return this$static;
}

function $SimplePanel_0(this$static, elem){
  this$static.setElement(elem);
  return this$static;
}

function $add_4(this$static, w){
  if (this$static.widget !== null) {
    throw $IllegalStateException(new IllegalStateException(), 'SimplePanel can only contain one child widget');
  }
  this$static.setWidget(w);
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

function iterator_1(){
  return $SimplePanel$1(new SimplePanel$1(), this);
}

function remove_6(w){
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
_.iterator_0 = iterator_1;
_.remove_0 = remove_6;
_.setWidget = setWidget_1;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SimplePanel';
_.typeId$ = 14;
_.widget = null;
function $clinit_100(){
  $clinit_100 = nullMethod;
  impl_2 = new PopupImplIE6();
}

function $PopupPanel(this$static){
  $clinit_100();
  $SimplePanel_0(this$static, $createElement_0(impl_2));
  $setPopupPosition(this$static, 0, 0);
  return this$static;
}

function $PopupPanel_0(this$static, autoHide){
  $clinit_100();
  $PopupPanel(this$static);
  this$static.autoHide = autoHide;
  return this$static;
}

function $PopupPanel_1(this$static, autoHide, modal){
  $clinit_100();
  $PopupPanel_0(this$static, autoHide);
  this$static.modal = modal;
  return this$static;
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
    $show(this$static);
  }
  left = round_int((getClientWidth() - $getOffsetWidth(this$static)) / 2);
  top = round_int((getClientHeight() - $getOffsetHeight(this$static)) / 2);
  $setPopupPosition(this$static, getScrollLeft() + left, getScrollTop() + top);
  if (!initiallyShowing) {
    $setVisible(this$static, true);
  }
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
  $onHide(impl_2, this$static.getElement());
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
        allow = this$static.onKeyDownPreview(narrow_char(eventGetKeyCode(event_0)), getKeyboardModifiers(event_0));
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
        if (($clinit_35() , sCaptureElem) !== null) {
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

function $setVisible(this$static, visible){
  setStyleAttribute(this$static.getElement(), 'visibility', visible?'visible':'hidden');
  $setVisible_0(impl_2, this$static.getElement(), visible);
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

function $show(this$static){
  if (this$static.showing) {
    return;
  }
  this$static.showing = true;
  addEventPreview(this$static);
  setStyleAttribute(this$static.getElement(), 'position', 'absolute');
  if (this$static.topPosition != (-1)) {
    $setPopupPosition(this$static, this$static.leftPosition, this$static.topPosition);
  }
  $add_0(get(), this$static);
  $onShow(impl_2, this$static.getElement());
}

function getContainerElement(){
  return this.getElement();
}

function getStyleElement(){
  return this.getElement();
}

function onDetach_0(){
  removeEventPreview(this);
  $onDetach(this);
}

function onEventPreview_0(event_0){
  return $onEventPreview(this, event_0);
}

function onKeyDownPreview_0(key, modifiers){
  return true;
}

function setHeight(height){
  this.desiredHeight = height;
  $maybeUpdateSize(this);
  if ($length(height) == 0) {
    this.desiredHeight = null;
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
_.getStyleElement = getStyleElement;
_.onDetach = onDetach_0;
_.onEventPreview = onEventPreview_0;
_.onKeyDownPreview = onKeyDownPreview_0;
_.setHeight = setHeight;
_.setVisible = setVisible;
_.setWidget = setWidget_0;
_.setWidth = setWidth_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'PopupPanel';
_.typeId$ = 15;
_.autoHide = false;
_.desiredHeight = null;
_.desiredWidth = null;
_.leftPosition = (-1);
_.modal = false;
_.showing = false;
_.topPosition = (-1);
var impl_2;
function $clinit_59(){
  $clinit_59 = nullMethod;
  $clinit_100();
}

function $$init_7(this$static){
  this$static.caption = $HTML(new HTML());
  this$static.panel = $FlexTable(new FlexTable());
}

function $DialogBox(this$static){
  $clinit_59();
  $DialogBox_0(this$static, false);
  return this$static;
}

function $DialogBox_0(this$static, autoHide){
  $clinit_59();
  $DialogBox_1(this$static, autoHide, true);
  return this$static;
}

function $DialogBox_1(this$static, autoHide, modal){
  $clinit_59();
  $PopupPanel_1(this$static, autoHide, modal);
  $$init_7(this$static);
  $setWidget_0(this$static.panel, 0, 0, this$static.caption);
  this$static.panel.setHeight('100%');
  $setBorderWidth(this$static.panel, 0);
  $setCellPadding(this$static.panel, 0);
  $setCellSpacing(this$static.panel, 0);
  $setHeight(this$static.panel.cellFormatter, 1, 0, '100%');
  $setWidth(this$static.panel.cellFormatter, 1, 0, '100%');
  $setAlignment(this$static.panel.cellFormatter, 1, 0, ($clinit_80() , ALIGN_CENTER), ($clinit_84() , ALIGN_MIDDLE));
  $setWidget_1(this$static, this$static.panel);
  $setStyleName_0(this$static, 'gwt-DialogBox');
  $setStyleName_0(this$static.caption, 'Caption');
  $addMouseListener(this$static.caption, this$static);
  return this$static;
}

function $setText_0(this$static, text){
  $setText_2(this$static.caption, text);
}

function $setWidget(this$static, w){
  if (this$static.child !== null) {
    $remove_2(this$static.panel, this$static.child);
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

function remove_2(w){
  if (this.child !== w) {
    return false;
  }
  $remove_2(this.panel, w);
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
_.remove_0 = remove_2;
_.setWidget = setWidget;
_.setWidth = setWidth;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DialogBox';
_.typeId$ = 16;
_.child = null;
_.dragStartX = 0;
_.dragStartY = 0;
_.dragging = false;
function $clinit_7(){
  $clinit_7 = nullMethod;
  $clinit_59();
}

function $AboutDialog(this$static){
  var outer, text;
  $clinit_7();
  $DialogBox(this$static);
  $setText_0(this$static, 'About the Mail Sample');
  outer = $VerticalPanel(new VerticalPanel());
  text = $HTML_0(new HTML(), "This sample application demonstrates the construction of a complex user interface using GWT's built-in widgets.  Have a look at the code to see how easy it is to build your own apps!");
  $setStyleName_0(text, 'mail-AboutText');
  $add_7(outer, text);
  $add_7(outer, $Button_1(new Button(), 'Close', $AboutDialog$1(new AboutDialog$1(), this$static)));
  $setWidget(this$static, outer);
  return this$static;
}

function onKeyDownPreview(key, modifiers){
  switch (key) {
    case 13:
    case 27:
      $hide(this);
      break;
  }
  return true;
}

function AboutDialog(){
}

_ = AboutDialog.prototype = new DialogBox();
_.onKeyDownPreview = onKeyDownPreview;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'AboutDialog';
_.typeId$ = 17;
function $AboutDialog$1(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function onClick(sender){
  $hide(this.this$0);
}

function AboutDialog$1(){
}

_ = AboutDialog$1.prototype = new Object_0();
_.onClick = onClick;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'AboutDialog$1';
_.typeId$ = 18;
function $getElement(this$static){
  if (this$static.widget === null) {
    throw $IllegalStateException(new IllegalStateException(), 'initWidget() was never called in ' + getTypeName(this$static));
  }
  return this$static.element;
}

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
  return $getElement(this);
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
_.typeId$ = 19;
_.widget = null;
function $$init(this$static){
  this$static.contacts = initValues('[Lcom.google.gwt.sample.mail.client.Contacts$Contact;', 127, 21, [$Contacts$Contact(new Contacts$Contact(), 'Benoit Mandelbrot', 'benoit@example.com', this$static), $Contacts$Contact(new Contacts$Contact(), 'Albert Einstein', 'albert@example.com', this$static), $Contacts$Contact(new Contacts$Contact(), 'Rene Descartes', 'rene@example.com', this$static), $Contacts$Contact(new Contacts$Contact(), 'Bob Saget', 'bob@example.com', this$static), $Contacts$Contact(new Contacts$Contact(), 'Ludwig von Beethoven', 'ludwig@example.com', this$static), $Contacts$Contact(new Contacts$Contact(), 'Richard Feynman', 'richard@example.com', this$static), $Contacts$Contact(new Contacts$Contact(), 'Alan Turing', 'alan@example.com', this$static), $Contacts$Contact(new Contacts$Contact(), 'John von Neumann', 'john@example.com', this$static)]);
  this$static.panel = $VerticalPanel(new VerticalPanel());
}

function $Contacts(this$static, images){
  var i, outer;
  $$init(this$static);
  outer = $SimplePanel(new SimplePanel());
  outer.setWidget(this$static.panel);
  for (i = 0; i < this$static.contacts.length_0; ++i) {
    $addContact(this$static, this$static.contacts[i]);
  }
  $initWidget(this$static, outer);
  $setStyleName_0(this$static, 'mail-Contacts');
  return this$static;
}

function $addContact(this$static, contact){
  var link;
  link = $HTML_0(new HTML(), "<a href='javascript:;'>" + contact.name + '<\/a>');
  $add_7(this$static.panel, link);
  $addClickListener_0(link, $Contacts$1(new Contacts$1(), this$static, contact, link));
}

function Contacts(){
}

_ = Contacts.prototype = new Composite();
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Contacts';
_.typeId$ = 20;
function $Contacts$1(this$static, this$0, val$contact, val$link){
  this$static.this$0 = this$0;
  this$static.val$contact = val$contact;
  this$static.val$link = val$link;
  return this$static;
}

function onClick_0(sender){
  var left, popup, top;
  popup = $Contacts$ContactPopup(new Contacts$ContactPopup(), this.val$contact, this.this$0);
  left = $getAbsoluteLeft_0(this.val$link) + 14;
  top = $getAbsoluteTop_0(this.val$link) + 14;
  $setPopupPosition(popup, left, top);
  $show(popup);
}

function Contacts$1(){
}

_ = Contacts$1.prototype = new Object_0();
_.onClick = onClick_0;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Contacts$1';
_.typeId$ = 21;
function $Contacts$Contact(this$static, name, email, this$0){
  this$static.name = name;
  this$static.email = email;
  return this$static;
}

function Contacts$Contact(){
}

_ = Contacts$Contact.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Contacts$Contact';
_.typeId$ = 22;
_.email = null;
_.name = null;
function $clinit_9(){
  $clinit_9 = nullMethod;
  $clinit_100();
}

function $Contacts$ContactPopup(this$static, contact, this$0){
  var emailLabel, hp, inner, nameLabel;
  $clinit_9();
  $PopupPanel_0(this$static, true);
  inner = $VerticalPanel(new VerticalPanel());
  nameLabel = $Label_0(new Label(), contact.name);
  emailLabel = $Label_0(new Label(), contact.email);
  $add_7(inner, nameLabel);
  $add_7(inner, emailLabel);
  hp = $HorizontalPanel(new HorizontalPanel());
  $setSpacing(hp, 4);
  $add_3(hp, $createImage(($clinit_20() , defaultPhoto_SINGLETON)));
  $add_3(hp, inner);
  $add_4(this$static, hp);
  $setStyleName_0(this$static, 'mail-ContactPopup');
  $setStyleName_0(nameLabel, 'mail-ContactPopupName');
  $setStyleName_0(emailLabel, 'mail-ContactPopupEmail');
  return this$static;
}

function Contacts$ContactPopup(){
}

_ = Contacts$ContactPopup.prototype = new PopupPanel();
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Contacts$ContactPopup';
_.typeId$ = 23;
function $clinit_19(){
  $clinit_19 = nullMethod;
  images_0 = $Mail_Images_generatedBundle(new Mail_Images_generatedBundle());
}

function $$init_2(this$static){
  this$static.topPanel = $TopPanel(new TopPanel(), images_0);
  this$static.rightPanel = $VerticalPanel(new VerticalPanel());
  this$static.mailDetail = $MailDetail(new MailDetail());
  this$static.shortcuts = $Shortcuts(new Shortcuts(), images_0);
}

function $Mail(this$static){
  $clinit_19();
  $$init_2(this$static);
  return this$static;
}

function $displayItem(this$static, item){
  $setItem(this$static.mailDetail, item);
}

function $onModuleLoad(this$static){
  var outer;
  singleton = this$static;
  this$static.topPanel.setWidth('100%');
  this$static.mailList = $MailList(new MailList());
  this$static.mailList.setWidth('100%');
  $add_7(this$static.rightPanel, this$static.mailList);
  $add_7(this$static.rightPanel, this$static.mailDetail);
  this$static.mailList.setWidth('100%');
  this$static.mailDetail.setWidth('100%');
  outer = $DockPanel(new DockPanel());
  $add_2(outer, this$static.topPanel, ($clinit_63() , NORTH));
  $add_2(outer, this$static.shortcuts, ($clinit_63() , WEST));
  $add_2(outer, this$static.rightPanel, ($clinit_63() , CENTER));
  outer.setWidth('100%');
  $setSpacing(outer, 4);
  $setCellWidth(outer, this$static.rightPanel, '100%');
  addWindowResizeListener(this$static);
  enableScrolling(false);
  setMargin('0px');
  $add_0(get(), outer);
  addCommand($Mail$1(new Mail$1(), this$static));
  $onWindowResized(this$static, getClientWidth(), getClientHeight());
}

function $onWindowResized(this$static, width, height){
  var shortcutHeight;
  shortcutHeight = height - $getAbsoluteTop_0(this$static.shortcuts) - 8;
  if (shortcutHeight < 1) {
    shortcutHeight = 1;
  }
  this$static.shortcuts.setHeight('' + shortcutHeight);
  $adjustSize(this$static.mailDetail, width, height);
}

function onWindowResized(width, height){
  $onWindowResized(this, width, height);
}

function Mail(){
}

_ = Mail.prototype = new Object_0();
_.onWindowResized = onWindowResized;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Mail';
_.typeId$ = 24;
_.mailList = null;
var images_0, singleton = null;
function $Mail$1(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function execute(){
  $onWindowResized(this.this$0, getClientWidth(), getClientHeight());
}

function Mail$1(){
}

_ = Mail$1.prototype = new Object_0();
_.execute = execute;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Mail$1';
_.typeId$ = 25;
function $$init_0(this$static){
  this$static.panel = $VerticalPanel(new VerticalPanel());
  this$static.headerPanel = $VerticalPanel(new VerticalPanel());
  this$static.subject = $HTML(new HTML());
  this$static.sender = $HTML(new HTML());
  this$static.recipient = $HTML(new HTML());
  this$static.body_0 = $HTML(new HTML());
  this$static.scroller = $ScrollPanel_0(new ScrollPanel(), this$static.body_0);
}

function $MailDetail(this$static){
  var innerPanel;
  $$init_0(this$static);
  $setWordWrap_0(this$static.body_0, true);
  $add_7(this$static.headerPanel, this$static.subject);
  $add_7(this$static.headerPanel, this$static.sender);
  $add_7(this$static.headerPanel, this$static.recipient);
  this$static.headerPanel.setWidth('100%');
  innerPanel = $DockPanel(new DockPanel());
  $add_2(innerPanel, this$static.headerPanel, ($clinit_63() , NORTH));
  $add_2(innerPanel, this$static.scroller, ($clinit_63() , CENTER));
  $setCellHeight(innerPanel, this$static.scroller, '100%');
  $add_7(this$static.panel, innerPanel);
  $setSize(innerPanel, '100%', '100%');
  $setSize(this$static.scroller, '100%', '100%');
  $initWidget(this$static, this$static.panel);
  $setStyleName_0(this$static, 'mail-Detail');
  $setStyleName_0(this$static.headerPanel, 'mail-DetailHeader');
  $setStyleName_0(innerPanel, 'mail-DetailInner');
  $setStyleName_0(this$static.subject, 'mail-DetailSubject');
  $setStyleName_0(this$static.sender, 'mail-DetailSender');
  $setStyleName_0(this$static.recipient, 'mail-DetailRecipient');
  $setStyleName_0(this$static.body_0, 'mail-DetailBody');
  return this$static;
}

function $adjustSize(this$static, windowWidth, windowHeight){
  var scrollHeight, scrollWidth;
  scrollWidth = windowWidth - $getAbsoluteLeft_0(this$static.scroller) - 9;
  if (scrollWidth < 1) {
    scrollWidth = 1;
  }
  scrollHeight = windowHeight - $getAbsoluteTop_0(this$static.scroller) - 9;
  if (scrollHeight < 1) {
    scrollHeight = 1;
  }
  $setSize(this$static.scroller, '' + scrollWidth, '' + scrollHeight);
}

function $setItem(this$static, item){
  $setHTML_0(this$static.subject, item.subject);
  $setHTML_0(this$static.sender, '<b>From:<\/b>&nbsp;' + item.sender);
  $setHTML_0(this$static.recipient, '<b>To:<\/b>&nbsp;foo@example.com');
  $setHTML_0(this$static.body_0, item.body_0);
}

function MailDetail(){
}

_ = MailDetail.prototype = new Composite();
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'MailDetail';
_.typeId$ = 26;
function $MailItem(this$static, sender, email, subject, body){
  this$static.sender = sender;
  this$static.email = email;
  this$static.subject = subject;
  this$static.body_0 = body;
  return this$static;
}

function MailItem(){
}

_ = MailItem.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'MailItem';
_.typeId$ = 27;
_.body_0 = null;
_.email = null;
_.read = false;
_.sender = null;
_.subject = null;
function $clinit_17(){
  $clinit_17 = nullMethod;
  var i;
  senders = initValues('[Ljava.lang.String;', 124, 1, ['markboland05', 'Hollie Voss', 'boticario', 'Emerson Milton', 'Healy Colette', 'Brigitte Cobb', 'Elba Lockhart', 'Claudio Engle', 'Dena Pacheco', 'Brasil s.p', 'Parker', 'derbvktqsr', 'qetlyxxogg', 'antenas.sul', 'Christina Blake', 'Gail Horton', 'Orville Daniel', 'PostMaster', 'Rae Childers', 'Buster misjenou', 'user31065', 'ftsgeolbx', 'aqlovikigd', 'user18411', 'Mildred Starnes', 'Candice Carson', 'Louise Kelchner', 'Emilio Hutchinson', 'Geneva Underwood', 'Residence Oper?', 'fpnztbwag', 'tiger', 'Heriberto Rush', 'bulrush Bouchard', 'Abigail Louis', 'Chad Andrews', 'bjjycpaa', 'Terry English', 'Bell Snedden', 'huang', 'hhh', '(unknown sender)', 'Kent', 'Dirk Newman', 'Equipe Virtual Cards', 'wishesundmore', 'Benito Meeks']);
  emails = initValues('[Ljava.lang.String;', 124, 1, ['mark@example.com', 'hollie@example.com', 'boticario@example.com', 'emerson@example.com', 'healy@example.com', 'brigitte@example.com', 'elba@example.com', 'claudio@example.com', 'dena@example.com', 'brasilsp@example.com', 'parker@example.com', 'derbvktqsr@example.com', 'qetlyxxogg@example.com', 'antenas_sul@example.com', 'cblake@example.com', 'gailh@example.com', 'orville@example.com', 'post_master@example.com', 'rchilders@example.com', 'buster@example.com', 'user31065@example.com', 'ftsgeolbx@example.com', 'aqlovikigd@example.com', 'user18411@example.com', 'mildred@example.com', 'candice@example.com', 'louise_kelchner@example.com', 'emilio@example.com', 'geneva@example.com', 'residence_oper@example.com', 'fpnztbwag@example.com', 'tiger@example.com', 'heriberto@example.com', 'bulrush@example.com', 'abigail_louis@example.com', 'chada@example.com', 'bjjycpaa@example.com', 'terry@example.com', 'bell@example.com', 'huang@example.com', 'hhh@example.com', 'kent@example.com', 'newman@example.com', 'equipe_virtual@example.com', 'wishesundmore@example.com', 'benito@example.com']);
  subjects = initValues('[Ljava.lang.String;', 124, 1, ['URGENT -[Mon, 24 Apr 2006 02:17:27 +0000]', 'URGENT TRANSACTION -[Sun, 23 Apr 2006 13:10:03 +0000]', 'fw: Here it comes', 'voce ganho um vale presente Boticario', 'Read this ASAP', 'Hot Stock Talk', 'New Breed of Equity Trader', 'FWD: TopWeeks the wire special pr news release', '[fwd] Read this ASAP', 'Renda Extra R$1.000,00-R$2.000,00/m?s', 're: Make sure your special pr news released', 'Forbidden Knowledge Conference', 'decodificadores os menores pre?os', 're: Our Pick', 'RE: The hottest pick Watcher', 'RE: St0kkMarrkett Picks Trade watch special pr news release', 'St0kkMarrkett Picks Watch special pr news release news', 'You are a Winner oskoxmshco', 'Encrypted E-mail System (VIRUS REMOVED)', 'Fw: Malcolm', 'Secure Message System (VIRUS REMOVED)', 'fwd: St0kkMarrkett Picks Watch special pr news releaser', 'FWD: Financial Market Traderr special pr news release', '? s? uma dica r?pida !!!!! leia !!!', 're: You have to heard this', 'fwd: Watcher TopNews', 'VACANZE alle Mauritius', 'funny', 're: You need to review this', '[re:] Our Pick', 'RE: Before the be11 special pr news release', '[re:] Market TradePicks Trade watch news', 'No prescription needed', 'Seu novo site', '[fwd] Financial Market Trader Picker', 'FWD: Top Financial Market Specialists Trader interest increases', 'Os cart?es mais animados da web!!', 'We will sale 4 you cebtdbwtcv', 'RE: Best Top Financial Market Specialists Trader Picks']);
  fragments = initValues('[Ljava.lang.String;', 124, 1, ['Dear Friend,<br><br>I am Mr. Mark Boland the Bank Manager of ABN AMRO BANK 101 Moorgate, London, EC2M 6SB.<br><br>', 'I have an urgent and very confidential business proposition for you. On July 20, 2001; Mr. Zemenu Gente, a National of France, who used to be a private contractor with the Shell Petroleum Development Company in Saudi Arabia. Mr. Zemenu Gente Made a Numbered time (Fixed deposit) for 36 calendar months, valued at GBP?30, 000,000.00 (Thirty Million Pounds only) in my Branch.', 'I have all necessary legal documents that can be used to back up any claim we may make. All I require is your honest Co-operation, Confidentiality and A trust to enable us sees this transaction through. I guarantee you that this will be executed under a legitimate arrangement that will protect you from any breach of the law. Please get in touch with me urgently by E-mail and Provide me with the following;<br>', 'The OIL sector is going crazy. This is our weekly gift to you!<br><br>Get KKPT First Thing, This Is Going To Run!<br><br>Check out Latest NEWS!<br><br>KOKO PETROLEUM (KKPT) - This is our #1 pick for next week!<br>Our last pick gained $2.16 in 4 days of trading.<br>', 'LAS VEGAS, NEVADA--(MARKET WIRE)--Apr 6, 2006 -- KOKO Petroleum, Inc. (Other OTC:KKPT.PK - News) -<br>KOKO Petroleum, Inc. announced today that its operator for the Corsicana Field, JMT Resources, Ltd. ("JMT") will commence a re-work program on its Pecan Gap wells in the next week. The re-work program will consist of drilling six lateral bore production strings from the existing well bore. This process, known as Radial Jet Enhancement, will utilize high pressure fluids to drill the lateral well bores, which will extend out approximately 350\' each.', 'JMT has contracted with Well Enhancement Services, LLC (www.wellenhancement.com) to perform the rework on its Pierce nos. 14 and 14a. A small sand frac will follow the drilling of the lateral well bores in order to enhance permeability and create larger access to the Pecan Gap reservoir. Total cost of the re-work per well is estimated to be approximately $50,000 USD.', 'Parab?ns!<br>Voc? Ganhou Um Vale Presente da Botic?rio no valor de R$50,00<br>Voc? foi contemplado na Promo??o Respeite Minha Natureza - Pulseira Social.<br>Algu?m pode t?-lo inscrito na promo??o! (Amigos(as), Namorado(a) etc.).<br>Para retirar o seu pr?mio em uma das nossas Lojas, fa?a o download do Vale-Presente abaixo.<br>Ap?s o download, com o arquivo previamente salvo, imprima uma folha e salve a c?pia em seu computador para evitar transtornos decorrentes da perda do mesmo. Lembramos que o Vale-Presente ? ?nico e intransfer?vel.', 'Large Marketing Campaign running this weekend!<br><br>Should you get in today before it explodes?<br><br>This Will Fly Starting Monday!', 'PREMIER INFORMATION (PIFR)<br>A U.S. based company offers specialized information management serices to both the Insurance and Healthcare Industries. The services we provide are specific to each industry and designed for quick response and maximum security.<br><br>STK- PIFR<br>Current Price: .20<br>This one went to $2.80 during the last marketing Campaign!', 'These partnerships specifically allow Premier to obtain personal health information, as governed by the Health In-surancee Portability and Accountability Act of 1996 (HIPAA), and other applicable state laws and regulations.<br><br>Global HealthCare Market Undergoing Digital Conversion', '>>   Componentes e decodificadores; confira aqui;<br> http://br.geocities.com/listajohn/index.htm<br>', 'THE GOVERNING AWARD<br>NETHERLANDS HEAD OFFICE<br>AC 76892 HAUITSOP<br>AMSTERDAM, THE NETHERLANDS.<br>FROM: THE DESK OF THE PROMOTIONS MANAGER.<br>INTERNATIONAL PROMOTIONS / PRIZE AWARD DEPARTMENT<br>REF NUMBER: 14235/089.<br>BATCH NUMBER: 304/64780/IFY.<br>RE/AWARD NOTIFICATION<br>', "We are pleased to inform you of the announcement today 13th of April 2006, you among TWO LUCKY WINNERS WON the GOVERNING AWARD draw held on the 28th of March 2006. The THREE Winning Addresses were randomly selected from a batch of 10,000,000 international email addresses. Your email address emerged alongside TWO others as a category B winner in this year's Annual GOVERNING AWARD Draw.<br>", '>> obrigado por me dar esta pequena aten??o !!!<br>CASO GOSTE DE ASSISTIR TV , MAS A SUA ANTENA S? PEGA AQUELES CANAIS LOCAIS  OU O SEU SISTEMA PAGO ? MUITO CARO , SAIBA QUE TENHO CART?ES DE ACESSO PARA SKY DIRECTV , E DECODERS PARA  NET TVA E TECSAT , TUDO GRATIS , SEM ASSINTURA , SEM MENSALIDADE, VC PAGA UMA VEZ S? E ASSISTE A MUITOS CANAIS , FILMES , JOGOS , PORNOS , DESENHOS , DOCUMENT?RIOS ,SHOWS , ETC,<br><br>CART?O SKY E DIRECTV TOTALMENTE HACKEADOS  350,00<br>DECODERS NET TVA DESBLOQUEADOS                       390,00<br>KITS COMPLETOS SKY OU DTV ANTENA DECODER E CART?O  650,00<br>TECSAT FREE   450,00<br>TENHO TB ACESS?RIOS , CABOS, LNB .<br>', '********************************************************************<br> Original filename: mail.zip<br> Virus discovered: JS.Feebs.AC<br>********************************************************************<br> A file that was attached to this email contained a virus.<br> It is very likely that the original message was generated<br> by the virus and not a person - treat this message as you would<br> any other junk mail (spam).<br> For more information on why you received this message please visit:<br>', 'Put a few letters after your name. Let us show you how you can do it in just a few days.<br><br>http://thewrongchoiceforyou.info<br><br>kill future mailing by pressing this : see main website', "We possess scores of pharmaceutical products handy<br>All med's are made in U.S. laboratories<br>For your wellbeing! Very rapid, protected and secure<br>Ordering, No script required. We have the pain aid you require<br>", '"Oh, don\'t speak to me of Austria. Perhaps I don\'t understand things, but Austria never has wished, and does not wish, for war. She is betraying us! Russia alone must save Europe. Our gracious sovereign recognizes his high vocation and will be true to it. That is the one thing I have faith in! Our good and wonderful sovereign has to perform the noblest role on earth, and he is so virtuous and noble that God will not forsake him. He will fulfill his vocation and crush the hydra of revolution, which has become more terrible than ever in the person of this murderer and villain! We alone must avenge the blood of the just one.... Whom, I ask you, can we rely on?... England with her commercial spirit will not and cannot understand the Emperor Alexander\'s loftiness of soul. She has refused to evacuate Malta. She wanted to find, and still seeks, some secret motive in our actions. What answer did Novosiltsev get? None. The English have not understood and cannot understand the self-ab!<br>negation of our Emperor who wants nothing for himself, but only desires the good of mankind. And what have they promised? Nothing! And what little they have promised they will not perform! Prussia has always declared that Buonaparte is invincible, and that all Europe is powerless before him.... And I don\'t believe a word that Hardenburg says, or Haugwitz either. This famous Prussian neutrality is just a trap. I have faith only in God and the lofty destiny of our adored monarch. He will save Europe!"<br>"Those were extremes, no doubt, but they are not what is most important. What is important are the rights of man, emancipation from prejudices, and equality of citizenship, and all these ideas Napoleon has retained in full force."']);
  items = $ArrayList(new ArrayList());
  {
    for (i = 0; i < 37; ++i) {
      $add_9(items, createFakeMail());
    }
  }
}

function createFakeMail(){
  $clinit_17();
  var body, email, i, sender, subject;
  sender = senders[senderIdx++];
  if (senderIdx == senders.length_0) {
    senderIdx = 0;
  }
  email = emails[emailIdx++];
  if (emailIdx == emails.length_0) {
    emailIdx = 0;
  }
  subject = subjects[subjectIdx++];
  if (subjectIdx == subjects.length_0) {
    subjectIdx = 0;
  }
  body = '';
  for (i = 0; i < 10; ++i) {
    body += fragments[fragmentIdx++];
    if (fragmentIdx == fragments.length_0) {
      fragmentIdx = 0;
    }
  }
  return $MailItem(new MailItem(), sender, email, subject, body);
}

function getMailItem(index){
  $clinit_17();
  if (index >= items.size) {
    return null;
  }
  return dynamicCast($get_0(items, index), 4);
}

function getMailItemCount(){
  $clinit_17();
  return items.size;
}

var emailIdx = 0, emails, fragmentIdx = 0, fragments, items, senderIdx = 0, senders, subjectIdx = 0, subjects;
function $$init_1(this$static){
  this$static.countLabel = $HTML(new HTML());
  this$static.newerButton = $HTML_1(new HTML(), "<a href='javascript:;'>&lt; newer<\/a>", true);
  this$static.olderButton = $HTML_1(new HTML(), "<a href='javascript:;'>older &gt;<\/a>", true);
  this$static.table = $FlexTable(new FlexTable());
  this$static.navBar = $HorizontalPanel(new HorizontalPanel());
}

function $MailList(this$static){
  var innerNavBar;
  $$init_1(this$static);
  $setCellSpacing(this$static.table, 0);
  $setCellPadding(this$static.table, 0);
  this$static.table.setWidth('100%');
  $addTableListener(this$static.table, this$static);
  $addClickListener_0(this$static.newerButton, this$static);
  $addClickListener_0(this$static.olderButton, this$static);
  innerNavBar = $HorizontalPanel(new HorizontalPanel());
  $setStyleName_0(this$static.navBar, 'mail-ListNavBar');
  $add_3(innerNavBar, this$static.newerButton);
  $add_3(innerNavBar, this$static.countLabel);
  $add_3(innerNavBar, this$static.olderButton);
  $setHorizontalAlignment_0(this$static.navBar, ($clinit_80() , ALIGN_RIGHT));
  $add_3(this$static.navBar, innerNavBar);
  this$static.navBar.setWidth('100%');
  $initWidget(this$static, this$static.table);
  $setStyleName_0(this$static, 'mail-List');
  $initTable(this$static);
  $update(this$static);
  return this$static;
}

function $initTable(this$static){
  var i;
  $setText_1(this$static.table, 0, 0, 'Sender');
  $setText_1(this$static.table, 0, 1, 'Email');
  $setText_1(this$static.table, 0, 2, 'Subject');
  $setWidget_0(this$static.table, 0, 3, this$static.navBar);
  $setStyleName(this$static.table.rowFormatter, 0, 'mail-ListHeader');
  for (i = 0; i < 10; ++i) {
    $setText_1(this$static.table, i + 1, 0, '');
    $setText_1(this$static.table, i + 1, 1, '');
    $setText_1(this$static.table, i + 1, 2, '');
    $setWordWrap(this$static.table.cellFormatter, i + 1, 0, false);
    $setWordWrap(this$static.table.cellFormatter, i + 1, 1, false);
    $setWordWrap(this$static.table.cellFormatter, i + 1, 2, false);
    $setColSpan(this$static.table.cellFormatter, i + 1, 2, 2);
  }
}

function $selectRow(this$static, row){
  var item;
  item = getMailItem(this$static.startIndex + row);
  if (item === null) {
    return;
  }
  $styleRow(this$static, this$static.selectedRow, false);
  $styleRow(this$static, row, true);
  item.read = true;
  this$static.selectedRow = row;
  $displayItem(($clinit_19() , singleton), item);
}

function $styleRow(this$static, row, selected){
  if (row != (-1)) {
    if (selected) {
      $addStyleName(this$static.table.rowFormatter, row + 1, 'mail-SelectedRow');
    }
     else {
      $removeStyleName(this$static.table.rowFormatter, row + 1, 'mail-SelectedRow');
    }
  }
}

function $update(this$static){
  var count, i, item, max;
  count = getMailItemCount();
  max = this$static.startIndex + 10;
  if (max > count) {
    max = count;
  }
  this$static.newerButton.setVisible(this$static.startIndex != 0);
  this$static.olderButton.setVisible(this$static.startIndex + 10 < count);
  $setText_2(this$static.countLabel, '' + (this$static.startIndex + 1) + ' - ' + max + ' of ' + count);
  i = 0;
  for (; i < 10; ++i) {
    if (this$static.startIndex + i >= getMailItemCount()) {
      break;
    }
    item = getMailItem(this$static.startIndex + i);
    $setText_1(this$static.table, i + 1, 0, item.sender);
    $setText_1(this$static.table, i + 1, 1, item.email);
    $setText_1(this$static.table, i + 1, 2, item.subject);
  }
  for (; i < 10; ++i) {
    $setHTML(this$static.table, i + 1, 0, '&nbsp;');
    $setHTML(this$static.table, i + 1, 1, '&nbsp;');
    $setHTML(this$static.table, i + 1, 2, '&nbsp;');
  }
  if (this$static.selectedRow == (-1)) {
    $selectRow(this$static, 0);
  }
}

function onCellClicked(sender, row, cell){
  if (row > 0) {
    $selectRow(this, row - 1);
  }
}

function onClick_1(sender){
  if (sender === this.olderButton) {
    this.startIndex += 10;
    if (this.startIndex >= getMailItemCount()) {
      this.startIndex -= 10;
    }
     else {
      $styleRow(this, this.selectedRow, false);
      this.selectedRow = (-1);
      $update(this);
    }
  }
   else if (sender === this.newerButton) {
    this.startIndex -= 10;
    if (this.startIndex < 0) {
      this.startIndex = 0;
    }
     else {
      $styleRow(this, this.selectedRow, false);
      this.selectedRow = (-1);
      $update(this);
    }
  }
}

function MailList(){
}

_ = MailList.prototype = new Composite();
_.onCellClicked = onCellClicked;
_.onClick = onClick_1;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'MailList';
_.typeId$ = 28;
_.selectedRow = (-1);
_.startIndex = 0;
function $clinit_20(){
  $clinit_20 = nullMethod;
  IMAGE_BUNDLE_URL = getModuleBaseURL() + 'B9DA8B0768BAD7283674A8E1D92AD03D.cache.png';
  contactsgroup_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 0, 0, 32, 32);
  defaultPhoto_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 32, 0, 32, 32);
  drafts_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 64, 0, 16, 16);
  home_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 80, 0, 16, 16);
  inbox_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 96, 0, 16, 16);
  leftCorner_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 112, 0, 4, 4);
  logo_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 116, 0, 140, 75);
  mailgroup_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 256, 0, 32, 32);
  rightCorner_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 288, 0, 4, 4);
  sent_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 292, 0, 16, 16);
  tasksgroup_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 308, 0, 32, 32);
  templates_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 340, 0, 16, 16);
  trash_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 356, 0, 16, 16);
  treeClosed_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 372, 0, 16, 16);
  treeLeaf_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 388, 0, 16, 16);
  treeOpen_SINGLETON = $ClippedImagePrototype(new ClippedImagePrototype(), IMAGE_BUNDLE_URL, 404, 0, 16, 16);
}

function $Mail_Images_generatedBundle(this$static){
  $clinit_20();
  return this$static;
}

function Mail_Images_generatedBundle(){
}

_ = Mail_Images_generatedBundle.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Mail_Images_generatedBundle';
_.typeId$ = 29;
var IMAGE_BUNDLE_URL, contactsgroup_SINGLETON, defaultPhoto_SINGLETON, drafts_SINGLETON, home_SINGLETON, inbox_SINGLETON, leftCorner_SINGLETON, logo_SINGLETON, mailgroup_SINGLETON, rightCorner_SINGLETON, sent_SINGLETON, tasksgroup_SINGLETON, templates_SINGLETON, trash_SINGLETON, treeClosed_SINGLETON, treeLeaf_SINGLETON, treeOpen_SINGLETON;
function $Mailboxes(this$static, images){
  var root;
  this$static.tree = $Tree(new Tree(), images);
  root = $TreeItem_0(new TreeItem(), $imageItemHTML(this$static, ($clinit_20() , home_SINGLETON), 'foo@example.com'));
  $addItem_0(this$static.tree, root);
  $addImageItem(this$static, root, 'Inbox', ($clinit_20() , inbox_SINGLETON));
  $addImageItem(this$static, root, 'Drafts', ($clinit_20() , drafts_SINGLETON));
  $addImageItem(this$static, root, 'Templates', ($clinit_20() , templates_SINGLETON));
  $addImageItem(this$static, root, 'Sent', ($clinit_20() , sent_SINGLETON));
  $addImageItem(this$static, root, 'Trash', ($clinit_20() , trash_SINGLETON));
  $setState(root, true);
  $initWidget(this$static, this$static.tree);
  return this$static;
}

function $addImageItem(this$static, root, title, imageProto){
  var item;
  item = $TreeItem_0(new TreeItem(), $imageItemHTML(this$static, imageProto, title));
  root.addItem(item);
  return item;
}

function $imageItemHTML(this$static, imageProto, title){
  return '<span>' + $getHTML_0(imageProto) + title + '<\/span>';
}

function Mailboxes(){
}

_ = Mailboxes.prototype = new Composite();
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Mailboxes';
_.typeId$ = 30;
_.tree = null;
function $$init_3(this$static){
  this$static.stackPanel = $Shortcuts$1(new Shortcuts$1(), this$static);
}

function $Shortcuts(this$static, images){
  $$init_3(this$static);
  $add(this$static, images, $Mailboxes(new Mailboxes(), images), ($clinit_20() , mailgroup_SINGLETON), 'Mail');
  $add(this$static, images, $Tasks(new Tasks()), ($clinit_20() , tasksgroup_SINGLETON), 'Tasks');
  $add(this$static, images, $Contacts(new Contacts(), images), ($clinit_20() , contactsgroup_SINGLETON), 'Contacts');
  $initWidget(this$static, this$static.stackPanel);
  return this$static;
}

function $add(this$static, images, widget, imageProto, caption){
  $addStyleName_0(widget, 'mail-StackContent');
  $add_6(this$static.stackPanel, widget, $createHeaderHTML(this$static, images, imageProto, caption), true);
}

function $computeHeaderId(this$static, index){
  return 'header-' + this$static.hashCode$() + '-' + index;
}

function $createHeaderHTML(this$static, images, imageProto, caption){
  var captionHTML, cssId, isTop;
  isTop = this$static.nextHeaderIndex == 0;
  cssId = $computeHeaderId(this$static, this$static.nextHeaderIndex);
  this$static.nextHeaderIndex++;
  captionHTML = "<table class='caption' cellpadding='0' cellspacing='0'><tr><td class='lcaption'>" + $getHTML_0(imageProto) + "<\/td><td class='rcaption'><b style='white-space:nowrap'>" + caption + '<\/b><\/td><\/tr><\/table>';
  return "<table id='" + cssId + "' align='left' cellpadding='0' cellspacing='0'" + (isTop?" class='is-top'":'') + '><tbody>' + "<tr><td class='box-00'>" + $getHTML_0(($clinit_20() , leftCorner_SINGLETON)) + '<\/td>' + "<td class='box-10'>&nbsp;<\/td>" + "<td class='box-20'>" + $getHTML_0(($clinit_20() , rightCorner_SINGLETON)) + '<\/td>' + '<\/tr><tr>' + "<td class='box-01'>&nbsp;<\/td>" + "<td class='box-11'>" + captionHTML + '<\/td>' + "<td class='box-21'>&nbsp;<\/td>" + '<\/tr><\/tbody><\/table>';
}

function $updateSelectedStyles(this$static, oldIndex, newIndex){
  var elem;
  oldIndex++;
  if (oldIndex > 0 && oldIndex < this$static.stackPanel.children_0.size) {
    elem = getElementById($computeHeaderId(this$static, oldIndex));
    setElementProperty(elem, 'className', '');
  }
  newIndex++;
  if (newIndex > 0 && newIndex < this$static.stackPanel.children_0.size) {
    elem = getElementById($computeHeaderId(this$static, newIndex));
    setElementProperty(elem, 'className', 'is-beneath-selected');
  }
}

function onLoad(){
  $showStack(this.stackPanel, 0);
  $updateSelectedStyles(this, (-1), 0);
}

function Shortcuts(){
}

_ = Shortcuts.prototype = new Composite();
_.onLoad = onLoad;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Shortcuts';
_.typeId$ = 31;
_.nextHeaderIndex = 0;
function $$init_6(this$static){
  this$static.children_0 = $WidgetCollection(new WidgetCollection(), this$static);
}

function $ComplexPanel(this$static){
  $$init_6(this$static);
  return this$static;
}

function $add_1(this$static, child, container){
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
  $insert_1(this$static.children_0, child, beforeIndex);
  if (domInsert) {
    insertChild(container, $getElement(child), beforeIndex);
  }
   else {
    appendChild(container, $getElement(child));
  }
  $adopt(this$static, child);
}

function $remove_1(this$static, w){
  var elem;
  if (w.parent !== this$static) {
    return false;
  }
  $orphan(this$static, w);
  elem = w.getElement();
  removeChild(getParent(elem), elem);
  $remove_6(this$static.children_0, w);
  return true;
}

function iterator(){
  return $iterator_0(this.children_0);
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
_.typeId$ = 32;
function $StackPanel(this$static){
  var table;
  $ComplexPanel(this$static);
  table = createTable();
  this$static.setElement(table);
  this$static.body_0 = createTBody();
  appendChild(table, this$static.body_0);
  setElementPropertyInt(table, 'cellSpacing', 0);
  setElementPropertyInt(table, 'cellPadding', 0);
  sinkEvents(table, 1);
  $setStyleName_0(this$static, 'gwt-StackPanel');
  return this$static;
}

function $add_5(this$static, w){
  $insert_0(this$static, w, this$static.children_0.size);
}

function $add_6(this$static, w, stackText, asHTML){
  $add_5(this$static, w);
  $setStackText(this$static, this$static.children_0.size - 1, stackText, asHTML);
}

function $findDividerIndex(this$static, elem){
  var expando, ownerHash;
  while (elem !== null && !compare(elem, this$static.getElement())) {
    expando = getElementProperty(elem, '__index');
    if (expando !== null) {
      ownerHash = getElementPropertyInt(elem, '__owner');
      if (ownerHash == this$static.hashCode$()) {
        return parseInt_0(expando);
      }
       else {
        return (-1);
      }
    }
    elem = getParent(elem);
  }
  return (-1);
}

function $insert_0(this$static, w, beforeIndex){
  var effectiveIndex, tdb, tdh, trb, trh;
  trh = createTR();
  tdh = createTD();
  appendChild(trh, tdh);
  trb = createTR();
  tdb = createTD();
  appendChild(trb, tdb);
  beforeIndex = $adjustIndex(this$static, w, beforeIndex);
  effectiveIndex = beforeIndex * 2;
  insertChild(this$static.body_0, trb, effectiveIndex);
  insertChild(this$static.body_0, trh, effectiveIndex);
  setStyleName_0(tdh, 'gwt-StackPanelItem', true);
  setElementPropertyInt(tdh, '__owner', this$static.hashCode$());
  setElementProperty(tdh, 'height', '1px');
  setElementProperty(tdb, 'height', '100%');
  setElementProperty(tdb, 'vAlign', 'top');
  $insert(this$static, w, tdb, beforeIndex, false);
  $updateIndicesFrom(this$static, beforeIndex);
  if (this$static.visibleStack == (-1)) {
    $showStack(this$static, 0);
  }
   else {
    $setStackVisible(this$static, beforeIndex, false);
    if (this$static.visibleStack >= beforeIndex) {
      ++this$static.visibleStack;
    }
  }
}

function $onBrowserEvent(this$static, event_0){
  var index, target;
  if (eventGetType(event_0) == 1) {
    target = eventGetTarget(event_0);
    index = $findDividerIndex(this$static, target);
    if (index != (-1)) {
      $showStack(this$static, index);
    }
  }
}

function $remove_3(this$static, child, index){
  var removed, rowIndex, tr;
  removed = $remove_1(this$static, child);
  if (removed) {
    rowIndex = 2 * index;
    tr = getChild(this$static.body_0, rowIndex);
    removeChild(this$static.body_0, tr);
    tr = getChild(this$static.body_0, rowIndex);
    removeChild(this$static.body_0, tr);
    if (this$static.visibleStack == index) {
      this$static.visibleStack = (-1);
    }
     else if (this$static.visibleStack > index) {
      --this$static.visibleStack;
    }
    $updateIndicesFrom(this$static, rowIndex);
  }
  return removed;
}

function $setStackText(this$static, index, text, asHTML){
  var td;
  if (index >= this$static.children_0.size) {
    return;
  }
  td = getChild(getChild(this$static.body_0, index * 2), 0);
  if (asHTML) {
    setInnerHTML(td, text);
  }
   else {
    setInnerText(td, text);
  }
}

function $setStackVisible(this$static, index, visible){
  var td, tr;
  tr = getChild(this$static.body_0, index * 2);
  if (tr === null) {
    return;
  }
  td = getFirstChild(tr);
  setStyleName_0(td, 'gwt-StackPanelItem-selected', visible);
  tr = getChild(this$static.body_0, index * 2 + 1);
  setVisible_0(tr, visible);
  $getWidget(this$static, index).setVisible(visible);
}

function $showStack(this$static, index){
  if (index >= this$static.children_0.size || index == this$static.visibleStack) {
    return;
  }
  if (this$static.visibleStack >= 0) {
    $setStackVisible(this$static, this$static.visibleStack, false);
  }
  this$static.visibleStack = index;
  $setStackVisible(this$static, this$static.visibleStack, true);
}

function $updateIndicesFrom(this$static, beforeIndex){
  var c, childTD, childTR, i;
  for (i = beforeIndex , c = this$static.children_0.size; i < c; ++i) {
    childTR = getChild(this$static.body_0, i * 2);
    childTD = getFirstChild(childTR);
    setElementPropertyInt(childTD, '__index', i);
  }
}

function onBrowserEvent_5(event_0){
  $onBrowserEvent(this, event_0);
}

function remove_7(child){
  return $remove_3(this, child, $getWidgetIndex(this, child));
}

function StackPanel(){
}

_ = StackPanel.prototype = new ComplexPanel();
_.onBrowserEvent = onBrowserEvent_5;
_.remove_0 = remove_7;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'StackPanel';
_.typeId$ = 33;
_.body_0 = null;
_.visibleStack = (-1);
function $Shortcuts$1(this$static, this$0){
  this$static.this$0 = this$0;
  $StackPanel(this$static);
  return this$static;
}

function onBrowserEvent(event_0){
  var newIndex, oldIndex;
  oldIndex = this.visibleStack;
  $onBrowserEvent(this, event_0);
  newIndex = this.visibleStack;
  if (oldIndex != newIndex) {
    $updateSelectedStyles(this.this$0, oldIndex, newIndex);
  }
}

function Shortcuts$1(){
}

_ = Shortcuts$1.prototype = new StackPanel();
_.onBrowserEvent = onBrowserEvent;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Shortcuts$1';
_.typeId$ = 34;
function $Tasks(this$static){
  var list, panel;
  panel = $SimplePanel(new SimplePanel());
  list = $VerticalPanel(new VerticalPanel());
  panel.setWidget(list);
  $add_7(list, $CheckBox_1(new CheckBox(), 'Get groceries'));
  $add_7(list, $CheckBox_1(new CheckBox(), 'Walk the dog'));
  $add_7(list, $CheckBox_1(new CheckBox(), 'Start Web 2.0 company'));
  $add_7(list, $CheckBox_1(new CheckBox(), 'Write cool app in GWT'));
  $add_7(list, $CheckBox_1(new CheckBox(), 'Get funding'));
  $add_7(list, $CheckBox_1(new CheckBox(), 'Take a vacation'));
  $initWidget(this$static, panel);
  $setStyleName_0(this$static, 'mail-Tasks');
  return this$static;
}

function Tasks(){
}

_ = Tasks.prototype = new Composite();
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'Tasks';
_.typeId$ = 35;
function $$init_4(this$static){
  this$static.signOutLink = $HTML_0(new HTML(), "<a href='javascript:;'>Sign Out<\/a>");
  this$static.aboutLink = $HTML_0(new HTML(), "<a href='javascript:;'>About<\/a>");
}

function $TopPanel(this$static, images){
  var inner, links, logo, outer;
  $$init_4(this$static);
  outer = $HorizontalPanel(new HorizontalPanel());
  inner = $VerticalPanel(new VerticalPanel());
  $setHorizontalAlignment_0(outer, ($clinit_80() , ALIGN_RIGHT));
  $setHorizontalAlignment_1(inner, ($clinit_80() , ALIGN_RIGHT));
  links = $HorizontalPanel(new HorizontalPanel());
  $setSpacing(links, 4);
  $add_3(links, this$static.signOutLink);
  $add_3(links, this$static.aboutLink);
  logo = $createImage(($clinit_20() , logo_SINGLETON));
  $add_3(outer, logo);
  outer.setCellHorizontalAlignment(logo, ($clinit_80() , ALIGN_LEFT));
  $add_3(outer, inner);
  $add_7(inner, $HTML_0(new HTML(), '<b>Welcome back, foo@example.com<\/b>'));
  $add_7(inner, links);
  $addClickListener_0(this$static.signOutLink, this$static);
  $addClickListener_0(this$static.aboutLink, this$static);
  $initWidget(this$static, outer);
  $setStyleName_0(this$static, 'mail-TopPanel');
  $setStyleName_0(links, 'mail-TopPanelLinks');
  return this$static;
}

function onClick_2(sender){
  var dlg;
  if (sender === this.signOutLink) {
    alert('If this were implemented, you would be signed out now.');
  }
   else if (sender === this.aboutLink) {
    dlg = $AboutDialog(new AboutDialog());
    $show(dlg);
    $center(dlg);
  }
}

function TopPanel(){
}

_ = TopPanel.prototype = new Composite();
_.onClick = onClick_2;
_.typeName$ = package_com_google_gwt_sample_mail_client_ + 'TopPanel';
_.typeId$ = 36;
function $Throwable(this$static, message){
  message;
  return this$static;
}

function Throwable(){
}

_ = Throwable.prototype = new Object_0();
_.typeName$ = package_java_lang_ + 'Throwable';
_.typeId$ = 3;
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
_.typeId$ = 37;
function $$init_5(this$static){
  this$static.cancellationTimer = $CommandExecutor$1(new CommandExecutor$1(), this$static);
  this$static.commands = $ArrayList(new ArrayList());
  this$static.executionTimer = $CommandExecutor$2(new CommandExecutor$2(), this$static);
  this$static.iterator = $CommandExecutor$CircularIterator(new CommandExecutor$CircularIterator(), this$static);
}

function $CommandExecutor(this$static){
  $$init_5(this$static);
  return this$static;
}

function $doCommandCanceled(this$static){
  var cmd, ex, ueh;
  cmd = $getLast(this$static.iterator);
  $remove(this$static.iterator);
  ex = null;
  if (instanceOf(cmd, 5)) {
    ex = $CommandCanceledException(new CommandCanceledException(), dynamicCast(cmd, 5));
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
        if (instanceOf(element, 5)) {
          command = dynamicCast(element, 5);
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
_.typeId$ = 38;
_.executing = false;
_.executionTimerPending = false;
function $clinit_42(){
  $clinit_42 = nullMethod;
  timers = $ArrayList(new ArrayList());
  {
    hookWindowClosing();
  }
}

function $Timer(this$static){
  $clinit_42();
  return this$static;
}

function $cancel(this$static){
  if (this$static.isRepeating) {
    clearInterval(this$static.timerId);
  }
   else {
    clearTimeout(this$static.timerId);
  }
  $remove_9(timers, this$static);
}

function $fireImpl(this$static){
  if (!this$static.isRepeating) {
    $remove_9(timers, this$static);
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
  $clinit_42();
  $wnd.clearInterval(id);
}

function clearTimeout(id){
  $clinit_42();
  $wnd.clearTimeout(id);
}

function createTimeout(timer, delay){
  $clinit_42();
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
  $clinit_42();
  addWindowCloseListener(new Timer$1());
}

function Timer(){
}

_ = Timer.prototype = new Object_0();
_.fire = fire;
_.typeName$ = package_com_google_gwt_user_client_ + 'Timer';
_.typeId$ = 39;
_.isRepeating = false;
_.timerId = 0;
var timers;
function $clinit_30(){
  $clinit_30 = nullMethod;
  $clinit_42();
}

function $CommandExecutor$1(this$static, this$0){
  $clinit_30();
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
_.typeId$ = 40;
function $clinit_31(){
  $clinit_31 = nullMethod;
  $clinit_42();
}

function $CommandExecutor$2(this$static, this$0){
  $clinit_31();
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
_.typeId$ = 41;
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
  $remove_8(this$static.this$0.commands, this$static.last);
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
_.typeId$ = 42;
_.end = 0;
_.last = (-1);
_.next = 0;
function $clinit_35(){
  $clinit_35 = nullMethod;
  sEventPreviewStack = $ArrayList(new ArrayList());
  {
    impl = new DOMImplIE6();
    $init(impl);
  }
}

function addEventPreview(preview){
  $clinit_35();
  $add_9(sEventPreviewStack, preview);
}

function appendChild(parent, child){
  $clinit_35();
  $appendChild(impl, parent, child);
}

function compare(elem1, elem2){
  $clinit_35();
  return $compare(impl, elem1, elem2);
}

function createButton(){
  $clinit_35();
  return $createElement(impl, 'button');
}

function createDiv(){
  $clinit_35();
  return $createElement(impl, 'div');
}

function createElement(tagName){
  $clinit_35();
  return $createElement(impl, tagName);
}

function createImg(){
  $clinit_35();
  return $createElement(impl, 'img');
}

function createInputCheck(){
  $clinit_35();
  return $createInputElement(impl, 'checkbox');
}

function createLabel(){
  $clinit_35();
  return $createElement(impl, 'label');
}

function createSpan(){
  $clinit_35();
  return $createElement(impl, 'span');
}

function createTBody(){
  $clinit_35();
  return $createElement(impl, 'tbody');
}

function createTD(){
  $clinit_35();
  return $createElement(impl, 'td');
}

function createTR(){
  $clinit_35();
  return $createElement(impl, 'tr');
}

function createTable(){
  $clinit_35();
  return $createElement(impl, 'table');
}

function dispatchEvent(evt, elem, listener){
  $clinit_35();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    dispatchEventImpl(evt, elem, listener);
  }
}

function dispatchEventImpl(evt, elem, listener){
  $clinit_35();
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
  $clinit_35();
  $eventCancelBubble(impl, evt, cancel);
}

function eventGetAltKey(evt){
  $clinit_35();
  return $eventGetAltKey(impl, evt);
}

function eventGetClientX(evt){
  $clinit_35();
  return $eventGetClientX(impl, evt);
}

function eventGetClientY(evt){
  $clinit_35();
  return $eventGetClientY(impl, evt);
}

function eventGetCtrlKey(evt){
  $clinit_35();
  return $eventGetCtrlKey(impl, evt);
}

function eventGetCurrentTarget(evt){
  $clinit_35();
  return $eventGetCurrentTarget(impl, evt);
}

function eventGetFromElement(evt){
  $clinit_35();
  return $eventGetFromElement(impl, evt);
}

function eventGetKeyCode(evt){
  $clinit_35();
  return $eventGetKeyCode(impl, evt);
}

function eventGetMetaKey(evt){
  $clinit_35();
  return $eventGetMetaKey(impl, evt);
}

function eventGetShiftKey(evt){
  $clinit_35();
  return $eventGetShiftKey(impl, evt);
}

function eventGetTarget(evt){
  $clinit_35();
  return $eventGetTarget(impl, evt);
}

function eventGetToElement(evt){
  $clinit_35();
  return $eventGetToElement(impl, evt);
}

function eventGetType(evt){
  $clinit_35();
  return $eventGetTypeInt(impl, evt);
}

function eventPreventDefault(evt){
  $clinit_35();
  $eventPreventDefault(impl, evt);
}

function getAbsoluteLeft(elem){
  $clinit_35();
  return $getAbsoluteLeft(impl, elem);
}

function getAbsoluteTop(elem){
  $clinit_35();
  return $getAbsoluteTop(impl, elem);
}

function getChild(parent, index){
  $clinit_35();
  return $getChild(impl, parent, index);
}

function getChildCount(parent){
  $clinit_35();
  return $getChildCount(impl, parent);
}

function getChildIndex(parent, child){
  $clinit_35();
  return $getChildIndex(impl, parent, child);
}

function getElementById(id){
  $clinit_35();
  return $getElementById(impl, id);
}

function getElementProperty(elem, prop){
  $clinit_35();
  return $getElementProperty(impl, elem, prop);
}

function getElementPropertyBoolean(elem, prop){
  $clinit_35();
  return $getElementPropertyBoolean(impl, elem, prop);
}

function getElementPropertyInt(elem, prop){
  $clinit_35();
  return $getElementPropertyInt(impl, elem, prop);
}

function getEventsSunk(elem){
  $clinit_35();
  return $getEventsSunk(impl, elem);
}

function getFirstChild(elem){
  $clinit_35();
  return $getFirstChild(impl, elem);
}

function getParent(elem){
  $clinit_35();
  return $getParent(impl, elem);
}

function insertChild(parent, child, index){
  $clinit_35();
  $insertChild(impl, parent, child, index);
}

function isOrHasChild(parent, child){
  $clinit_35();
  return $isOrHasChild(impl, parent, child);
}

function previewEvent(evt){
  $clinit_35();
  var preview, ret;
  ret = true;
  if (sEventPreviewStack.size > 0) {
    preview = dynamicCast($get_0(sEventPreviewStack, sEventPreviewStack.size - 1), 6);
    if (!(ret = preview.onEventPreview(evt))) {
      eventCancelBubble(evt, true);
      eventPreventDefault(evt);
    }
  }
  return ret;
}

function releaseCapture(elem){
  $clinit_35();
  if (sCaptureElem !== null && compare(elem, sCaptureElem)) {
    sCaptureElem = null;
  }
  $releaseCapture(impl, elem);
}

function removeChild(parent, child){
  $clinit_35();
  $removeChild(impl, parent, child);
}

function removeEventPreview(preview){
  $clinit_35();
  $remove_9(sEventPreviewStack, preview);
}

function scrollIntoView(elem){
  $clinit_35();
  $scrollIntoView(impl, elem);
}

function setCapture(elem){
  $clinit_35();
  sCaptureElem = elem;
  $setCapture(impl, elem);
}

function setElementProperty(elem, prop, value){
  $clinit_35();
  $setElementProperty(impl, elem, prop, value);
}

function setElementPropertyBoolean(elem, prop, value){
  $clinit_35();
  $setElementPropertyBoolean(impl, elem, prop, value);
}

function setElementPropertyInt(elem, prop, value){
  $clinit_35();
  $setElementPropertyInt(impl, elem, prop, value);
}

function setEventListener(elem, listener){
  $clinit_35();
  $setEventListener(impl, elem, listener);
}

function setImgSrc(img, src){
  $clinit_35();
  $setImgSrc(impl, img, src);
}

function setInnerHTML(elem, html){
  $clinit_35();
  $setInnerHTML(impl, elem, html);
}

function setInnerText(elem, text){
  $clinit_35();
  $setInnerText(impl, elem, text);
}

function setIntStyleAttribute(elem, attr, value){
  $clinit_35();
  $setIntStyleAttribute(impl, elem, attr, value);
}

function setStyleAttribute(elem, attr, value){
  $clinit_35();
  $setStyleAttribute(impl, elem, attr, value);
}

function sinkEvents(elem, eventBits){
  $clinit_35();
  $sinkEvents(impl, elem, eventBits);
}

function windowGetClientHeight(){
  $clinit_35();
  return $windowGetClientHeight(impl);
}

function windowGetClientWidth(){
  $clinit_35();
  return $windowGetClientWidth(impl);
}

var currentEvent = null, impl = null, sCaptureElem = null, sEventPreviewStack;
function $clinit_36(){
  $clinit_36 = nullMethod;
  commandExecutor = $CommandExecutor(new CommandExecutor());
}

function addCommand(cmd){
  $clinit_36();
  if (cmd === null) {
    throw $NullPointerException(new NullPointerException(), 'cmd can not be null');
  }
  $submit(commandExecutor, cmd);
}

var commandExecutor;
function $equals_0(this$static, other){
  if (instanceOf(other, 7)) {
    return compare(this$static, dynamicCast(other, 7));
  }
  return $equals(wrapJSO(this$static, Element), other);
}

function equals_0(other){
  return $equals_0(this, other);
}

function hashCode_1(){
  return $hashCode(wrapJSO(this, Element));
}

function Element(){
}

_ = Element.prototype = new JavaScriptObject();
_.equals$ = equals_0;
_.hashCode$ = hashCode_1;
_.typeName$ = package_com_google_gwt_user_client_ + 'Element';
_.typeId$ = 43;
function equals_1(other){
  return $equals(wrapJSO(this, Event), other);
}

function hashCode_2(){
  return $hashCode(wrapJSO(this, Event));
}

function Event(){
}

_ = Event.prototype = new JavaScriptObject();
_.equals$ = equals_1;
_.hashCode$ = hashCode_2;
_.typeName$ = package_com_google_gwt_user_client_ + 'Event';
_.typeId$ = 44;
function onWindowClosed(){
  while (($clinit_42() , timers).size > 0) {
    $cancel(dynamicCast($get_0(($clinit_42() , timers), 0), 8));
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
_.typeId$ = 45;
function $clinit_45(){
  $clinit_45 = nullMethod;
  closingListeners = $ArrayList(new ArrayList());
  resizeListeners = $ArrayList(new ArrayList());
  {
    init();
  }
}

function addWindowCloseListener(listener){
  $clinit_45();
  $add_9(closingListeners, listener);
}

function addWindowResizeListener(listener){
  $clinit_45();
  $add_9(resizeListeners, listener);
}

function alert(msg){
  $clinit_45();
  $wnd.alert(msg);
}

function enableScrolling(enable){
  $clinit_45();
  $doc.body.style.overflow = enable?'auto':'hidden';
}

function fireClosedImpl(){
  $clinit_45();
  var it, listener;
  for (it = $iterator_1(closingListeners); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 9);
    listener.onWindowClosed();
  }
}

function fireClosingImpl(){
  $clinit_45();
  var it, listener, msg, ret;
  ret = null;
  for (it = $iterator_1(closingListeners); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 9);
    msg = listener.onWindowClosing();
    {
      ret = msg;
    }
  }
  return ret;
}

function fireResizedImpl(){
  $clinit_45();
  var it, listener;
  for (it = $iterator_1(resizeListeners); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 10);
    listener.onWindowResized(getClientWidth(), getClientHeight());
  }
}

function getClientHeight(){
  $clinit_45();
  return windowGetClientHeight();
}

function getClientWidth(){
  $clinit_45();
  return windowGetClientWidth();
}

function getScrollLeft(){
  $clinit_45();
  return $doc.documentElement.scrollLeft || $doc.body.scrollLeft;
}

function getScrollTop(){
  $clinit_45();
  return $doc.documentElement.scrollTop || $doc.body.scrollTop;
}

function init(){
  $clinit_45();
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
  $clinit_45();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    fireClosedImpl();
  }
}

function onClosing(){
  $clinit_45();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    return fireClosingImpl();
  }
}

function onResize(){
  $clinit_45();
  var handler;
  handler = sUncaughtExceptionHandler;
  {
    fireResizedImpl();
  }
}

function setMargin(size){
  $clinit_45();
  $doc.body.style.margin = size;
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

function $eventCancelBubble(this$static, evt, cancel){
  evt.cancelBubble = cancel;
}

function $eventGetAltKey(this$static, evt){
  return !(!evt.altKey);
}

function $eventGetCtrlKey(this$static, evt){
  return !(!evt.ctrlKey);
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

function $getElementById(this$static, id){
  var elem = $doc.getElementById(id);
  return elem || null;
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

function $removeChild(this$static, parent, child){
  parent.removeChild(child);
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

function $setInnerHTML(this$static, elem, html){
  if (!html) {
    html = '';
  }
  elem.innerHTML = html;
}

function $setIntStyleAttribute(this$static, elem, attr, value){
  elem.style[attr] = value;
}

function $setStyleAttribute(this$static, elem, attr, value){
  elem.style[attr] = value;
}

function $windowGetClientHeight(this$static){
  return $doc.body.clientHeight;
}

function $windowGetClientWidth(this$static){
  return $doc.body.clientWidth;
}

function DOMImpl(){
}

_ = DOMImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_impl_ + 'DOMImpl';
_.typeId$ = 46;
function $compare(this$static, elem1, elem2){
  if (!elem1 && !elem2)
    return true;
  else if (!elem1 || !elem2)
    return false;
  return elem1.uniqueID == elem2.uniqueID;
}

function $eventGetClientX(this$static, evt){
  return evt.clientX - getBodyClientLeft();
}

function $eventGetClientY(this$static, evt){
  return evt.clientY - getBodyClientTop();
}

function $eventGetCurrentTarget(this$static, evt){
  return currentEventTarget;
}

function $eventGetFromElement(this$static, evt){
  return evt.fromElement?evt.fromElement:null;
}

function $eventGetTarget(this$static, evt){
  return evt.srcElement || null;
}

function $eventGetToElement(this$static, evt){
  return evt.toElement || null;
}

function $eventPreventDefault(this$static, evt){
  evt.returnValue = false;
}

function $getAbsoluteLeft(this$static, elem){
  var scrollLeft = $doc.documentElement.scrollLeft || $doc.body.scrollLeft;
  return elem.getBoundingClientRect().left + scrollLeft - getBodyClientLeft();
}

function $getAbsoluteTop(this$static, elem){
  var scrollTop = $doc.documentElement.scrollTop || $doc.body.scrollTop;
  return elem.getBoundingClientRect().top + scrollTop - getBodyClientTop();
}

function $getChild(this$static, elem, index){
  var child = elem.children[index];
  return child || null;
}

function $getChildCount(this$static, elem){
  return elem.children.length;
}

function $getChildIndex(this$static, parent, child){
  var count = parent.children.length;
  for (var i = 0; i < count; ++i) {
    if (child.uniqueID == parent.children[i].uniqueID)
      return i;
  }
  return -1;
}

function $getFirstChild(this$static, elem){
  var child = elem.firstChild;
  return child || null;
}

function $getParent(this$static, elem){
  var parent = elem.parentElement;
  return parent || null;
}

function $init(this$static){
  try {
    $doc.execCommand('BackgroundImageCache', false, true);
  }
   catch (e) {
  }
  $wnd.__dispatchEvent = function(){
    var oldEventTarget = currentEventTarget;
    currentEventTarget = this;
    if ($wnd.event.returnValue == null) {
      $wnd.event.returnValue = true;
      if (!previewEvent($wnd.event)) {
        currentEventTarget = oldEventTarget;
        return;
      }
    }
    var listener, curElem = this;
    while (curElem && !(listener = curElem.__listener))
      curElem = curElem.parentElement;
    if (listener)
      dispatchEvent($wnd.event, curElem, listener);
    currentEventTarget = oldEventTarget;
  }
  ;
  $wnd.__dispatchDblClickEvent = function(){
    var newEvent = $doc.createEventObject();
    this.fireEvent('onclick', newEvent);
    if (this.__eventBits & 2)
      $wnd.__dispatchEvent.call(this);
  }
  ;
  $doc.body.onclick = $doc.body.onmousedown = $doc.body.onmouseup = $doc.body.onmousemove = $doc.body.onmousewheel = $doc.body.onkeydown = $doc.body.onkeypress = $doc.body.onkeyup = $doc.body.onfocus = $doc.body.onblur = $doc.body.ondblclick = $wnd.__dispatchEvent;
}

function $insertChild(this$static, parent, child, index){
  if (index >= parent.children.length)
    parent.appendChild(child);
  else 
    parent.insertBefore(child, parent.children[index]);
}

function $isOrHasChild(this$static, parent, child){
  while (child) {
    if (parent.uniqueID == child.uniqueID)
      return true;
    child = child.parentElement;
  }
  return false;
}

function $releaseCapture(this$static, elem){
  elem.releaseCapture();
}

function $setCapture(this$static, elem){
  elem.setCapture();
}

function $setImgSrc(this$static, img, src){
  setImgSrc_0(img, src);
}

function $setInnerText(this$static, elem, text){
  if (!text)
    text = '';
  elem.innerText = text;
}

function $sinkEvents(this$static, elem, bits){
  elem.__eventBits = bits;
  elem.onclick = bits & 1?$wnd.__dispatchEvent:null;
  elem.ondblclick = bits & (1 | 2)?$wnd.__dispatchDblClickEvent:null;
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

function getBodyClientLeft(){
  return $doc.documentElement.clientLeft || $doc.body.clientLeft;
}

function getBodyClientTop(){
  return $doc.documentElement.clientTop || $doc.body.clientTop;
}

function DOMImplIE6(){
}

_ = DOMImplIE6.prototype = new DOMImpl();
_.typeName$ = package_com_google_gwt_user_client_impl_ + 'DOMImplIE6';
_.typeId$ = 47;
var currentEventTarget = null;
function addChild(parent, child){
  parent.__kids.push(child);
  child.__pendingSrc = parent.__pendingSrc;
}

function addTop(srcImgMap, img, src){
  img.src = src;
  if (img.complete) {
    return;
  }
  img.__kids = [];
  img.__pendingSrc = src;
  srcImgMap[src] = img;
  var _onload = img.onload, _onerror = img.onerror, _onabort = img.onabort;
  function finish(_originalHandler){
    var kids = img.__kids;
    img.__cleanup();
    window.setTimeout(function(){
      for (var i = 0; i < kids.length; ++i) {
        var kid = kids[i];
        if (kid.__pendingSrc == src) {
          kid.src = src;
          kid.__pendingSrc = null;
        }
      }
    }
    , 0);
    _originalHandler && _originalHandler.call(img);
  }

  img.onload = function(){
    finish(_onload);
  }
  ;
  img.onerror = function(){
    finish(_onerror);
  }
  ;
  img.onabort = function(){
    finish(_onabort);
  }
  ;
  img.__cleanup = function(){
    img.onload = _onload;
    img.onerror = _onerror;
    img.onabort = _onabort;
    img.__cleanup = img.__pendingSrc = img.__kids = null;
    delete srcImgMap[src];
  }
  ;
}

function getImgSrc(img){
  return img.__pendingSrc || img.src;
}

function getPendingSrc(img){
  return img.__pendingSrc || null;
}

function getTop(srcImgMap, src){
  return srcImgMap[src] || null;
}

function removeChild_0(parent, child){
  var uniqueID = child.uniqueID;
  var kids = parent.__kids;
  for (var i = 0, c = kids.length; i < c; ++i) {
    if (kids[i].uniqueID == uniqueID) {
      kids.splice(i, 1);
      child.__pendingSrc = null;
      return;
    }
  }
}

function removeTop(srcImgMap, img){
  var src = img.__pendingSrc;
  var kids = img.__kids;
  img.__cleanup();
  if (img = kids[0]) {
    img.__pendingSrc = null;
    addTop(srcImgMap, img, src);
    if (img.__pendingSrc) {
      kids.splice(0, 1);
      img.__kids = kids;
    }
     else {
      for (var i = 1, c = kids.length; i < c; ++i) {
        kids[i].src = src;
        kids[i].__pendingSrc = null;
      }
    }
  }
}

function setImgSrc_0(img, src){
  var oldSrc, top;
  if ($equals_1(getImgSrc(img), src)) {
    return;
  }
  if (srcImgMap_0 === null) {
    srcImgMap_0 = createObject();
  }
  oldSrc = getPendingSrc(img);
  if (oldSrc !== null) {
    top = getTop(srcImgMap_0, oldSrc);
    if ($equals_0(top, wrapJSO(img, Element))) {
      removeTop(srcImgMap_0, top);
    }
     else {
      removeChild_0(top, img);
    }
  }
  top = getTop(srcImgMap_0, src);
  if (top === null) {
    addTop(srcImgMap_0, img, src);
  }
   else {
    addChild(top, img);
  }
}

var srcImgMap_0 = null;
function $AbsolutePanel(this$static){
  $ComplexPanel(this$static);
  this$static.setElement(createDiv());
  setStyleAttribute(this$static.getElement(), 'position', 'relative');
  setStyleAttribute(this$static.getElement(), 'overflow', 'hidden');
  return this$static;
}

function $add_0(this$static, w){
  $add_1(this$static, w, this$static.getElement());
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
_.typeId$ = 48;
function AbstractImagePrototype(){
}

_ = AbstractImagePrototype.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'AbstractImagePrototype';
_.typeId$ = 49;
function $clinit_67(){
  $clinit_67 = nullMethod;
  $clinit_134() , implWidget;
}

function $FocusWidget(this$static, elem){
  $clinit_134() , implWidget;
  $setElement(this$static, elem);
  return this$static;
}

function $addClickListener(this$static, listener){
  if (this$static.clickListeners === null) {
    this$static.clickListeners = $ClickListenerCollection(new ClickListenerCollection());
  }
  $add_9(this$static.clickListeners, listener);
}

function $setElement(this$static, elem){
  $setElement_1(this$static, elem);
  $sinkEvents_0(this$static, 7041);
}

function onBrowserEvent_0(event_0){
  switch (eventGetType(event_0)) {
    case 1:
      if (this.clickListeners !== null) {
        $fireClick(this.clickListeners, this);
      }

      break;
    case 4096:
    case 2048:
      break;
    case 128:
    case 512:
    case 256:
      break;
  }
}

function setElement(elem){
  $setElement(this, elem);
}

function FocusWidget(){
}

_ = FocusWidget.prototype = new Widget();
_.onBrowserEvent = onBrowserEvent_0;
_.setElement = setElement;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'FocusWidget';
_.typeId$ = 50;
_.clickListeners = null;
function $clinit_51(){
  $clinit_51 = nullMethod;
  $clinit_134() , implWidget;
}

function $ButtonBase(this$static, elem){
  $clinit_134() , implWidget;
  $FocusWidget(this$static, elem);
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
_.typeId$ = 51;
function $clinit_52(){
  $clinit_52 = nullMethod;
  $clinit_134() , implWidget;
}

function $Button(this$static){
  $clinit_134() , implWidget;
  $ButtonBase(this$static, createButton());
  adjustType(this$static.getElement());
  $setStyleName_0(this$static, 'gwt-Button');
  return this$static;
}

function $Button_0(this$static, html){
  $clinit_134() , implWidget;
  $Button(this$static);
  this$static.setHTML(html);
  return this$static;
}

function $Button_1(this$static, html, listener){
  $clinit_134() , implWidget;
  $Button_0(this$static, html);
  $addClickListener(this$static, listener);
  return this$static;
}

function adjustType(button){
  $clinit_52();
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
_.typeId$ = 52;
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

function setCellHorizontalAlignment(w, align){
  var td;
  td = $getWidgetTd(this, w);
  if (td !== null) {
    $setCellHorizontalAlignment(this, td, align);
  }
}

function CellPanel(){
}

_ = CellPanel.prototype = new ComplexPanel();
_.setCellHorizontalAlignment = setCellHorizontalAlignment;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'CellPanel';
_.typeId$ = 53;
_.body_0 = null;
_.table = null;
function $clinit_54(){
  $clinit_54 = nullMethod;
  $clinit_134() , implWidget;
}

function $CheckBox(this$static){
  $clinit_134() , implWidget;
  $CheckBox_0(this$static, createInputCheck());
  $setStyleName_0(this$static, 'gwt-CheckBox');
  return this$static;
}

function $CheckBox_1(this$static, label){
  $clinit_134() , implWidget;
  $CheckBox(this$static);
  $setText(this$static, label);
  return this$static;
}

function $CheckBox_0(this$static, elem){
  var uid;
  $clinit_134() , implWidget;
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

function $setText(this$static, text){
  setInnerText(this$static.labelElem, text);
}

function onLoad_0(){
  setEventListener(this.inputElem, this);
}

function onUnload(){
  setEventListener(this.inputElem, null);
  $setChecked(this, $isChecked(this));
}

function setHTML_0(html){
  setInnerHTML(this.labelElem, html);
}

function CheckBox(){
}

_ = CheckBox.prototype = new ButtonBase();
_.onLoad = onLoad_0;
_.onUnload = onUnload;
_.setHTML = setHTML_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'CheckBox';
_.typeId$ = 54;
_.inputElem = null;
_.labelElem = null;
var uniqueId = 0;
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

function add_0(o){
  throw $UnsupportedOperationException(new UnsupportedOperationException(), 'add');
}

function contains(o){
  var iter;
  iter = $advanceToFind(this, this.iterator_0(), o);
  return iter !== null;
}

function toArray(a){
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

function AbstractCollection(){
}

_ = AbstractCollection.prototype = new Object_0();
_.add_0 = add_0;
_.contains = contains;
_.toArray = toArray;
_.typeName$ = package_java_util_ + 'AbstractCollection';
_.typeId$ = 55;
function $indexOutOfBounds(this$static, i){
  throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Index: ' + i + ', Size: ' + this$static.size);
}

function $iterator_1(this$static){
  return $AbstractList$IteratorImpl(new AbstractList$IteratorImpl(), this$static);
}

function add_1(index, element){
  throw $UnsupportedOperationException(new UnsupportedOperationException(), 'add');
}

function add_2(obj){
  this.add(this.size_0(), obj);
  return true;
}

function equals_4(o){
  var elem, elemOther, iter, iterOther, other;
  if (o === this) {
    return true;
  }
  if (!instanceOf(o, 25)) {
    return false;
  }
  other = dynamicCast(o, 25);
  if (this.size_0() != other.size_0()) {
    return false;
  }
  iter = $iterator_1(this);
  iterOther = other.iterator_0();
  while ($hasNext_3(iter)) {
    elem = $next_2(iter);
    elemOther = $next_2(iterOther);
    if (!(elem === null?elemOther === null:elem.equals$(elemOther))) {
      return false;
    }
  }
  return true;
}

function hashCode_5(){
  var coeff, iter, k, obj;
  k = 1;
  coeff = 31;
  iter = $iterator_1(this);
  while ($hasNext_3(iter)) {
    obj = $next_2(iter);
    k = 31 * k + (obj === null?0:obj.hashCode$());
  }
  return k;
}

function iterator_3(){
  return $iterator_1(this);
}

function remove_10(index){
  throw $UnsupportedOperationException(new UnsupportedOperationException(), 'remove');
}

function AbstractList(){
}

_ = AbstractList.prototype = new AbstractCollection();
_.add = add_1;
_.add_0 = add_2;
_.equals$ = equals_4;
_.hashCode$ = hashCode_5;
_.iterator_0 = iterator_3;
_.remove = remove_10;
_.typeName$ = package_java_util_ + 'AbstractList';
_.typeId$ = 56;
function $$init_18(this$static){
  {
    $clearImpl(this$static);
  }
}

function $ArrayList(this$static){
  $$init_18(this$static);
  return this$static;
}

function $add_9(this$static, o){
  setImpl(this$static.array, this$static.size++, o);
  return true;
}

function $clearImpl(this$static){
  this$static.array = createArray();
  this$static.size = 0;
}

function $contains(this$static, o){
  return $indexOf_2(this$static, o) != (-1);
}

function $get_0(this$static, index){
  if (index < 0 || index >= this$static.size) {
    $indexOutOfBounds(this$static, index);
  }
  return getImpl(this$static.array, index);
}

function $indexOf_2(this$static, o){
  return $indexOf_3(this$static, o, 0);
}

function $indexOf_3(this$static, o, index){
  if (index < 0) {
    $indexOutOfBounds(this$static, index);
  }
  for (; index < this$static.size; ++index) {
    if (equals_7(o, getImpl(this$static.array, index))) {
      return index;
    }
  }
  return (-1);
}

function $isEmpty(this$static){
  return this$static.size == 0;
}

function $remove_8(this$static, index){
  var previous;
  previous = $get_0(this$static, index);
  removeRangeImpl(this$static.array, index, 1);
  --this$static.size;
  return previous;
}

function $remove_9(this$static, o){
  var i;
  i = $indexOf_2(this$static, o);
  if (i == (-1)) {
    return false;
  }
  $remove_8(this$static, i);
  return true;
}

function $set(this$static, index, o){
  var previous;
  previous = $get_0(this$static, index);
  setImpl(this$static.array, index, o);
  return previous;
}

function add_3(index, o){
  if (index < 0 || index > this.size) {
    $indexOutOfBounds(this, index);
  }
  addImpl(this.array, index, o);
  ++this.size;
}

function add_4(o){
  return $add_9(this, o);
}

function addImpl(array, index, o){
  array.splice(index, 0, o);
}

function contains_2(o){
  return $contains(this, o);
}

function equals_7(a, b){
  return a === b || a !== null && a.equals$(b);
}

function get_2(index){
  return $get_0(this, index);
}

function getImpl(array, index){
  return array[index];
}

function remove_11(index){
  return $remove_8(this, index);
}

function removeRangeImpl(array, index, count){
  array.splice(index, count);
}

function setImpl(array, index, o){
  array[index] = o;
}

function size_2(){
  return this.size;
}

function toArray_0(a){
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
_.add = add_3;
_.add_0 = add_4;
_.contains = contains_2;
_.get = get_2;
_.remove = remove_11;
_.size_0 = size_2;
_.toArray = toArray_0;
_.typeName$ = package_java_util_ + 'ArrayList';
_.typeId$ = 57;
_.array = null;
_.size = 0;
function $ClickListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireClick(this$static, sender){
  var it, listener;
  for (it = $iterator_1(this$static); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 11);
    listener.onClick(sender);
  }
}

function ClickListenerCollection(){
}

_ = ClickListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ClickListenerCollection';
_.typeId$ = 58;
function $clinit_63(){
  $clinit_63 = nullMethod;
  CENTER = new DockPanel$DockLayoutConstant();
  EAST = new DockPanel$DockLayoutConstant();
  NORTH = new DockPanel$DockLayoutConstant();
  SOUTH = new DockPanel$DockLayoutConstant();
  WEST = new DockPanel$DockLayoutConstant();
}

function $$init_8(this$static){
  this$static.horzAlign = ($clinit_80() , ALIGN_LEFT);
  this$static.vertAlign = ($clinit_84() , ALIGN_TOP);
}

function $DockPanel(this$static){
  $clinit_63();
  $CellPanel(this$static);
  $$init_8(this$static);
  setElementPropertyInt(this$static.table, 'cellSpacing', 0);
  setElementPropertyInt(this$static.table, 'cellPadding', 0);
  return this$static;
}

function $add_2(this$static, widget, direction){
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
  for (it = $iterator_0(this$static.children_0); $hasNext_1(it);) {
    child = $next_0(it);
    dir = child.layoutData.direction;
    if (dir === NORTH || dir === SOUTH) {
      ++rowCount;
    }
     else if (dir === EAST || dir === WEST) {
      ++colCount;
    }
  }
  rows = initDims_0('[Lcom.google.gwt.user.client.ui.DockPanel$TmpRow;', [128], [22], [rowCount], null);
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
  for (it = $iterator_0(this$static.children_0); $hasNext_1(it);) {
    child = $next_0(it);
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

function $setCellHeight(this$static, w, height){
  var data;
  data = w.layoutData;
  data.height_0 = height;
  if (data.td !== null) {
    setStyleAttribute(data.td, 'height', data.height_0);
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

function $setCellWidth(this$static, w, width){
  var data;
  data = w.layoutData;
  data.width_0 = width;
  if (data.td !== null) {
    setStyleAttribute(data.td, 'width', data.width_0);
  }
}

function remove_3(w){
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

function setCellHorizontalAlignment_0(w, align){
  $setCellHorizontalAlignment_0(this, w, align);
}

function DockPanel(){
}

_ = DockPanel.prototype = new CellPanel();
_.remove_0 = remove_3;
_.setCellHorizontalAlignment = setCellHorizontalAlignment_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DockPanel';
_.typeId$ = 59;
_.center = null;
var CENTER, EAST, NORTH, SOUTH, WEST;
function DockPanel$DockLayoutConstant(){
}

_ = DockPanel$DockLayoutConstant.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DockPanel$DockLayoutConstant';
_.typeId$ = 60;
function $DockPanel$LayoutData(this$static, dir){
  this$static.direction = dir;
  return this$static;
}

function DockPanel$LayoutData(){
}

_ = DockPanel$LayoutData.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'DockPanel$LayoutData';
_.typeId$ = 61;
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
_.typeId$ = 62;
_.center = 0;
_.tr = null;
function $$init_11(this$static){
  this$static.widgetMap = $HTMLTable$WidgetMapper(new HTMLTable$WidgetMapper());
}

function $HTMLTable(this$static){
  $$init_11(this$static);
  this$static.tableElem = createTable();
  this$static.bodyElem = createTBody();
  appendChild(this$static.tableElem, this$static.bodyElem);
  this$static.setElement(this$static.tableElem);
  $sinkEvents_0(this$static, 1);
  return this$static;
}

function $addTableListener(this$static, listener){
  if (this$static.tableListeners === null) {
    this$static.tableListeners = $TableListenerCollection(new TableListenerCollection());
  }
  $add_9(this$static.tableListeners, listener);
}

function $checkCellBounds(this$static, row, column){
  var cellSize;
  $checkRowBounds(this$static, row);
  if (column < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Column ' + column + ' must be non-negative: ' + column);
  }
  cellSize = $getCellCount(this$static, row);
  if (cellSize <= column) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Column index: ' + column + ', Column size: ' + $getCellCount(this$static, row));
  }
}

function $checkRowBounds(this$static, row){
  var rowSize;
  rowSize = $getRowCount(this$static);
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

function $getDOMCellCount(this$static, tableBody, row){
  return tableBody.rows[row].cells.length;
}

function $getDOMRowCount(this$static){
  return $getDOMRowCount_0(this$static, this$static.bodyElem);
}

function $getDOMRowCount_0(this$static, elem){
  return elem.rows.length;
}

function $getEventTargetCell(this$static, event_0){
  var body, td, tr;
  td = eventGetTarget(event_0);
  for (; td !== null; td = getParent(td)) {
    if ($equalsIgnoreCase(getElementProperty(td, 'tagName'), 'td')) {
      tr = getParent(td);
      body = getParent(tr);
      if (compare(body, this$static.bodyElem)) {
        return td;
      }
    }
    if (compare(td, this$static.bodyElem)) {
      return null;
    }
  }
  return null;
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
    $remove_2(this$static, widget);
    return true;
  }
   else {
    if (clearInnerHTML) {
      setInnerHTML(td, '');
    }
    return false;
  }
}

function $remove_2(this$static, widget){
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

function $setHTML(this$static, row, column, html){
  var td;
  $prepareCell(this$static, row, column);
  td = $cleanCell(this$static, row, column, html === null);
  if (html !== null) {
    setInnerHTML(td, html);
  }
}

function $setRowFormatter(this$static, rowFormatter){
  this$static.rowFormatter = rowFormatter;
}

function $setText_1(this$static, row, column, text){
  var td;
  $prepareCell(this$static, row, column);
  td = $cleanCell(this$static, row, column, text === null);
  if (text !== null) {
    setInnerText(td, text);
  }
}

function $setWidget_0(this$static, row, column, widget){
  var td;
  $prepareCell(this$static, row, column);
  if (widget !== null) {
    $removeFromParent(widget);
    td = $cleanCell(this$static, row, column, true);
    $putWidget(this$static.widgetMap, widget);
    appendChild(td, widget.getElement());
    $adopt(this$static, widget);
  }
}

function iterator_0(){
  return $widgetIterator(this.widgetMap);
}

function onBrowserEvent_1(event_0){
  var body, column, row, td, tr;
  switch (eventGetType(event_0)) {
    case 1:
      {
        if (this.tableListeners !== null) {
          td = $getEventTargetCell(this, event_0);
          if (td === null) {
            return;
          }
          tr = getParent(td);
          body = getParent(tr);
          row = getChildIndex(body, tr);
          column = getChildIndex(tr, td);
          $fireCellClicked(this.tableListeners, this, row, column);
        }
        break;
      }

    default:}
}

function remove_4(widget){
  return $remove_2(this, widget);
}

function HTMLTable(){
}

_ = HTMLTable.prototype = new Panel();
_.iterator_0 = iterator_0;
_.onBrowserEvent = onBrowserEvent_1;
_.remove_0 = remove_4;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable';
_.typeId$ = 63;
_.bodyElem = null;
_.cellFormatter = null;
_.columnFormatter = null;
_.rowFormatter = null;
_.tableElem = null;
_.tableListeners = null;
function $FlexTable(this$static){
  $HTMLTable(this$static);
  $setCellFormatter(this$static, $FlexTable$FlexCellFormatter(new FlexTable$FlexCellFormatter(), this$static));
  $setRowFormatter(this$static, $HTMLTable$RowFormatter(new HTMLTable$RowFormatter(), this$static));
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

function $prepareCell(this$static, row, column){
  var cellCount, required;
  $prepareRow(this$static, row);
  if (column < 0) {
    throw $IndexOutOfBoundsException(new IndexOutOfBoundsException(), 'Cannot create a column with a negative index: ' + column);
  }
  cellCount = $getCellCount(this$static, row);
  required = column + 1 - cellCount;
  if (required > 0) {
    addCells(this$static.bodyElem, row, required);
  }
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

function FlexTable(){
}

_ = FlexTable.prototype = new HTMLTable();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'FlexTable';
_.typeId$ = 64;
function $HTMLTable$CellFormatter(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $ensureElement(this$static, row, column){
  $prepareCell(this$static.this$0, row, column);
  return $getCellElement(this$static, this$static.this$0.bodyElem, row, column);
}

function $getCellElement(this$static, table, row, col){
  var out = table.rows[row].cells[col];
  return out == null?null:out;
}

function $getElement_0(this$static, row, column){
  $checkCellBounds(this$static.this$0, row, column);
  return $getCellElement(this$static, this$static.this$0.bodyElem, row, column);
}

function $getRawElement(this$static, row, column){
  return $getCellElement(this$static, this$static.this$0.bodyElem, row, column);
}

function $setAlignment(this$static, row, column, hAlign, vAlign){
  $setHorizontalAlignment(this$static, row, column, hAlign);
  $setVerticalAlignment(this$static, row, column, vAlign);
}

function $setHeight(this$static, row, column, height){
  var elem;
  $prepareCell(this$static.this$0, row, column);
  elem = $getCellElement(this$static, this$static.this$0.bodyElem, row, column);
  setElementProperty(elem, 'height', height);
}

function $setHorizontalAlignment(this$static, row, column, align){
  var elem;
  $prepareCell(this$static.this$0, row, column);
  elem = $getCellElement(this$static, this$static.this$0.bodyElem, row, column);
  setElementProperty(elem, 'align', align.textAlignString);
}

function $setVerticalAlignment(this$static, row, column, align){
  $prepareCell(this$static.this$0, row, column);
  setStyleAttribute($getCellElement(this$static, this$static.this$0.bodyElem, row, column), 'verticalAlign', align.verticalAlignString);
}

function $setWidth(this$static, row, column, width){
  $prepareCell(this$static.this$0, row, column);
  setElementProperty($getCellElement(this$static, this$static.this$0.bodyElem, row, column), 'width', width);
}

function $setWordWrap(this$static, row, column, wrap){
  var wrapValue;
  $prepareCell(this$static.this$0, row, column);
  wrapValue = wrap?'':'nowrap';
  setStyleAttribute($getElement_0(this$static, row, column), 'whiteSpace', wrapValue);
}

function HTMLTable$CellFormatter(){
}

_ = HTMLTable$CellFormatter.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$CellFormatter';
_.typeId$ = 65;
function $FlexTable$FlexCellFormatter(this$static, this$0){
  $HTMLTable$CellFormatter(this$static, this$0);
  return this$static;
}

function $setColSpan(this$static, row, column, colSpan){
  setElementPropertyInt($ensureElement(this$static, row, column), 'colSpan', colSpan);
}

function FlexTable$FlexCellFormatter(){
}

_ = FlexTable$FlexCellFormatter.prototype = new HTMLTable$CellFormatter();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'FlexTable$FlexCellFormatter';
_.typeId$ = 66;
function $clinit_66(){
  $clinit_66 = nullMethod;
  impl_0 = ($clinit_134() , implPanel);
}

var impl_0;
function $Label(this$static){
  this$static.setElement(createDiv());
  $sinkEvents_0(this$static, 131197);
  $setStyleName_0(this$static, 'gwt-Label');
  return this$static;
}

function $Label_0(this$static, text){
  $Label(this$static);
  $setText_2(this$static, text);
  return this$static;
}

function $addClickListener_0(this$static, listener){
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

function $setText_2(this$static, text){
  setInnerText(this$static.getElement(), text);
}

function $setWordWrap_0(this$static, wrap){
  setStyleAttribute(this$static.getElement(), 'whiteSpace', wrap?'normal':'nowrap');
}

function onBrowserEvent_3(event_0){
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
_.onBrowserEvent = onBrowserEvent_3;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Label';
_.typeId$ = 67;
_.clickListeners = null;
_.mouseListeners = null;
function $HTML(this$static){
  $Label(this$static);
  this$static.setElement(createDiv());
  $sinkEvents_0(this$static, 125);
  $setStyleName_0(this$static, 'gwt-HTML');
  return this$static;
}

function $HTML_0(this$static, html){
  $HTML(this$static);
  $setHTML_0(this$static, html);
  return this$static;
}

function $HTML_1(this$static, html, wordWrap){
  $HTML_0(this$static, html);
  $setWordWrap_0(this$static, wordWrap);
  return this$static;
}

function $setHTML_0(this$static, html){
  setInnerHTML(this$static.getElement(), html);
}

function HTML(){
}

_ = HTML.prototype = new Label();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTML';
_.typeId$ = 68;
function $$init_9(this$static){
  {
    $findNext(this$static);
  }
}

function $HTMLTable$1(this$static, this$1){
  this$static.this$1 = this$1;
  $$init_9(this$static);
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
_.typeId$ = 69;
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
_.typeId$ = 70;
_.columnGroup = null;
function $HTMLTable$RowFormatter(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $addStyleName(this$static, row, styleName){
  setStyleName_0($ensureElement_0(this$static, row), styleName, true);
}

function $ensureElement_0(this$static, row){
  $prepareRow(this$static.this$0, row);
  return $getRow(this$static, this$static.this$0.bodyElem, row);
}

function $getRow(this$static, elem, row){
  return elem.rows[row];
}

function $removeStyleName(this$static, row, styleName){
  setStyleName_0($ensureElement_0(this$static, row), styleName, false);
}

function $setStyleName(this$static, row, styleName){
  setStyleName($ensureElement_0(this$static, row), styleName);
}

function HTMLTable$RowFormatter(){
}

_ = HTMLTable$RowFormatter.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$RowFormatter';
_.typeId$ = 71;
function $$init_10(this$static){
  this$static.widgetList = $ArrayList(new ArrayList());
}

function $HTMLTable$WidgetMapper(this$static){
  $$init_10(this$static);
  return this$static;
}

function $getWidget_0(this$static, elem){
  var index;
  index = getWidgetIndex(elem);
  if (index < 0) {
    return null;
  }
  return dynamicCast($get_0(this$static.widgetList, index), 12);
}

function $putWidget(this$static, widget){
  var index;
  if (this$static.freeList === null) {
    index = this$static.widgetList.size;
    $add_9(this$static.widgetList, widget);
  }
   else {
    index = this$static.freeList.index;
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
_.typeId$ = 72;
_.freeList = null;
function $HTMLTable$WidgetMapper$FreeNode(this$static, index, next){
  this$static.index = index;
  this$static.next = next;
  return this$static;
}

function HTMLTable$WidgetMapper$FreeNode(){
}

_ = HTMLTable$WidgetMapper$FreeNode.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HTMLTable$WidgetMapper$FreeNode';
_.typeId$ = 73;
_.index = 0;
_.next = null;
function $clinit_80(){
  $clinit_80 = nullMethod;
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
_.typeId$ = 74;
_.textAlignString = null;
function $clinit_84(){
  $clinit_84 = nullMethod;
  $HasVerticalAlignment$VerticalAlignmentConstant(new HasVerticalAlignment$VerticalAlignmentConstant(), 'bottom');
  ALIGN_MIDDLE = $HasVerticalAlignment$VerticalAlignmentConstant(new HasVerticalAlignment$VerticalAlignmentConstant(), 'middle');
  ALIGN_TOP = $HasVerticalAlignment$VerticalAlignmentConstant(new HasVerticalAlignment$VerticalAlignmentConstant(), 'top');
}

var ALIGN_MIDDLE, ALIGN_TOP;
function $HasVerticalAlignment$VerticalAlignmentConstant(this$static, verticalAlignString){
  this$static.verticalAlignString = verticalAlignString;
  return this$static;
}

function HasVerticalAlignment$VerticalAlignmentConstant(){
}

_ = HasVerticalAlignment$VerticalAlignmentConstant.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HasVerticalAlignment$VerticalAlignmentConstant';
_.typeId$ = 75;
_.verticalAlignString = null;
function $$init_12(this$static){
  this$static.horzAlign = ($clinit_80() , ALIGN_LEFT);
  this$static.vertAlign = ($clinit_84() , ALIGN_TOP);
}

function $HorizontalPanel(this$static){
  $CellPanel(this$static);
  $$init_12(this$static);
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
  $add_1(this$static, w, td);
}

function $createAlignedTd(this$static){
  var td;
  td = createTD();
  $setCellHorizontalAlignment(this$static, td, this$static.horzAlign);
  $setCellVerticalAlignment(this$static, td, this$static.vertAlign);
  return td;
}

function $setHorizontalAlignment_0(this$static, align){
  this$static.horzAlign = align;
}

function remove_5(w){
  var removed, td;
  td = getParent(w.getElement());
  removed = $remove_1(this, w);
  if (removed) {
    removeChild(this.tableRow, td);
  }
  return removed;
}

function HorizontalPanel(){
}

_ = HorizontalPanel.prototype = new CellPanel();
_.remove_0 = remove_5;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'HorizontalPanel';
_.typeId$ = 76;
_.tableRow = null;
function $clinit_93(){
  $clinit_93 = nullMethod;
  $HashMap(new HashMap());
}

function $Image(this$static){
  $clinit_93();
  $changeState(this$static, $Image$UnclippedState(new Image$UnclippedState(), this$static));
  $setStyleName_0(this$static, 'gwt-Image');
  return this$static;
}

function $Image_0(this$static, url, left, top, width, height){
  $clinit_93();
  $changeState(this$static, $Image$ClippedState(new Image$ClippedState(), this$static, url, left, top, width, height));
  $setStyleName_0(this$static, 'gwt-Image');
  return this$static;
}

function $changeState(this$static, newState){
  this$static.state = newState;
}

function $setUrlAndVisibleRect(this$static, url, left, top, width, height){
  this$static.state.setUrlAndVisibleRect(this$static, url, left, top, width, height);
}

function onBrowserEvent_2(event_0){
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
_.onBrowserEvent = onBrowserEvent_2;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image';
_.typeId$ = 77;
_.state = null;
function execute_0(){
}

function Image$1(){
}

_ = Image$1.prototype = new Object_0();
_.execute = execute_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image$1';
_.typeId$ = 78;
function Image$State(){
}

_ = Image$State.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image$State';
_.typeId$ = 79;
function $clinit_89(){
  $clinit_89 = nullMethod;
  impl_1 = $ClippedImageImplIE6(new ClippedImageImplIE6());
}

function $Image$ClippedState(this$static, image, url, left, top, width, height){
  $clinit_89();
  this$static.left_0 = left;
  this$static.top_0 = top;
  this$static.width_0 = width;
  this$static.height_0 = height;
  this$static.url = url;
  image.setElement($createStructure(impl_1, url, left, top, width, height));
  $sinkEvents_0(image, 131197);
  $fireSyntheticLoadEvent(this$static, image);
  return this$static;
}

function $fireSyntheticLoadEvent(this$static, image){
  addCommand(new Image$1());
}

function setUrlAndVisibleRect(image, url, left, top, width, height){
  if (!$equals_1(this.url, url) || this.left_0 != left || this.top_0 != top || this.width_0 != width || this.height_0 != height) {
    this.url = url;
    this.left_0 = left;
    this.top_0 = top;
    this.width_0 = width;
    this.height_0 = height;
    $adjust(impl_1, image.getElement(), url, left, top, width, height);
    $fireSyntheticLoadEvent(this, image);
  }
}

function Image$ClippedState(){
}

_ = Image$ClippedState.prototype = new Image$State();
_.setUrlAndVisibleRect = setUrlAndVisibleRect;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image$ClippedState';
_.typeId$ = 80;
_.height_0 = 0;
_.left_0 = 0;
_.top_0 = 0;
_.url = null;
_.width_0 = 0;
var impl_1;
function $Image$UnclippedState(this$static, image){
  image.setElement(createImg());
  $sinkEvents_0(image, 229501);
  return this$static;
}

function setUrlAndVisibleRect_0(image, url, left, top, width, height){
  $changeState(image, $Image$ClippedState(new Image$ClippedState(), image, url, left, top, width, height));
}

function Image$UnclippedState(){
}

_ = Image$UnclippedState.prototype = new Image$State();
_.setUrlAndVisibleRect = setUrlAndVisibleRect_0;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Image$UnclippedState';
_.typeId$ = 81;
function getKeyboardModifiers(event_0){
  return (eventGetShiftKey(event_0)?1:0) | (eventGetMetaKey(event_0)?8:0) | (eventGetCtrlKey(event_0)?2:0) | (eventGetAltKey(event_0)?4:0);
}

function $MouseListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireMouseDown(this$static, sender, x, y){
  var it, listener;
  for (it = $iterator_1(this$static); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 13);
    listener.onMouseDown(sender, x, y);
  }
}

function $fireMouseEnter(this$static, sender){
  var it, listener;
  for (it = $iterator_1(this$static); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 13);
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
  for (it = $iterator_1(this$static); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 13);
    listener.onMouseLeave(sender);
  }
}

function $fireMouseMove(this$static, sender, x, y){
  var it, listener;
  for (it = $iterator_1(this$static); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 13);
    listener.onMouseMove(sender, x, y);
  }
}

function $fireMouseUp(this$static, sender, x, y){
  var it, listener;
  for (it = $iterator_1(this$static); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 13);
    listener.onMouseUp(sender, x, y);
  }
}

function MouseListenerCollection(){
}

_ = MouseListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'MouseListenerCollection';
_.typeId$ = 82;
function $clinit_102(){
  $clinit_102 = nullMethod;
  rootPanels = $HashMap(new HashMap());
}

function $RootPanel(this$static, elem){
  $clinit_102();
  $AbsolutePanel(this$static);
  if (elem === null) {
    elem = getBodyElement();
  }
  this$static.setElement(elem);
  this$static.onAttach();
  return this$static;
}

function get(){
  $clinit_102();
  return get_0(null);
}

function get_0(id){
  $clinit_102();
  var elem, gwt;
  gwt = dynamicCast($get_1(rootPanels, id), 14);
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
  $clinit_102();
  return $doc.body;
}

function hookWindowClosing_0(){
  $clinit_102();
  addWindowCloseListener(new RootPanel$1());
}

function RootPanel(){
}

_ = RootPanel.prototype = new AbsolutePanel();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'RootPanel';
_.typeId$ = 83;
var rootPanels;
function onWindowClosed_0(){
  var gwt, it;
  for (it = $iterator_3($values(($clinit_102() , rootPanels))); $hasNext_5(it);) {
    gwt = dynamicCast($next_4(it), 14);
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
_.typeId$ = 84;
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

function onBrowserEvent_4(event_0){
  eventGetType(event_0) == 16384;
}

function ScrollPanel(){
}

_ = ScrollPanel.prototype = new SimplePanel();
_.onBrowserEvent = onBrowserEvent_4;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'ScrollPanel';
_.typeId$ = 85;
function $$init_13(this$static){
  this$static.hasElement = this$static.this$0.widget !== null;
}

function $SimplePanel$1(this$static, this$0){
  this$static.this$0 = this$0;
  $$init_13(this$static);
  return this$static;
}

function hasNext_1(){
  return this.hasElement;
}

function next_2(){
  if (!this.hasElement || this.this$0.widget === null) {
    throw new NoSuchElementException();
  }
  this.hasElement = false;
  return this.this$0.widget;
}

function SimplePanel$1(){
}

_ = SimplePanel$1.prototype = new Object_0();
_.hasNext = hasNext_1;
_.next_0 = next_2;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'SimplePanel$1';
_.typeId$ = 86;
function $TableListenerCollection(this$static){
  $ArrayList(this$static);
  return this$static;
}

function $fireCellClicked(this$static, sender, row, cell){
  var it, listener;
  for (it = $iterator_1(this$static); $hasNext_3(it);) {
    listener = dynamicCast($next_2(it), 15);
    listener.onCellClicked(sender, row, cell);
  }
}

function TableListenerCollection(){
}

_ = TableListenerCollection.prototype = new ArrayList();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'TableListenerCollection';
_.typeId$ = 87;
function $$init_15(this$static){
  this$static.childWidgets = $HashMap(new HashMap());
}

function $Tree(this$static, images){
  $$init_15(this$static);
  this$static.images = images;
  this$static.setElement(createDiv());
  setStyleAttribute(this$static.getElement(), 'position', 'relative');
  this$static.focusable = $createFocusable(($clinit_66() , impl_0));
  setStyleAttribute(this$static.focusable, 'fontSize', '0');
  setStyleAttribute(this$static.focusable, 'position', 'absolute');
  setIntStyleAttribute(this$static.focusable, 'zIndex', (-1));
  appendChild(this$static.getElement(), this$static.focusable);
  $sinkEvents_0(this$static, 1021);
  sinkEvents(this$static.focusable, 6144);
  this$static.root = $Tree$1(new Tree$1(), this$static);
  $setTree(this$static.root, this$static);
  $setStyleName_0(this$static, 'gwt-Tree');
  return this$static;
}

function $addItem_0(this$static, item){
  $addItem(this$static.root, item);
}

function $collectElementChain(this$static, chain, hRoot, hElem){
  if (hElem === null || compare(hElem, hRoot)) {
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
  hCurElem = dynamicCast($get_0(chain, idx), 7);
  for (i = 0 , n = root.children_0.size; i < n; ++i) {
    child = $getChild_0(root, i);
    if (compare(child.getElement(), hCurElem)) {
      retItem = $findItemByChain(this$static, chain, idx + 1, $getChild_0(root, i));
      if (retItem === null) {
        return child;
      }
      return retItem;
    }
  }
  return $findItemByChain(this$static, chain, idx + 1, root);
}

function $iterator(this$static){
  var widgets;
  widgets = initDims_0('[Lcom.google.gwt.user.client.ui.Widget;', [125], [12], [this$static.childWidgets.size], null);
  $keySet(this$static.childWidgets).toArray(widgets);
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
    $focus(($clinit_66() , impl_0), this$static.focusable);
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
  idx = $getChildIndex_0(parent, sel);
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
  idx = $getChildIndex_0(parent, sel);
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
  }
}

function $removeItem_0(this$static, item){
  $removeItem(this$static.root, item);
}

function $setFocus(this$static, focus){
  if (focus) {
    $focus(($clinit_66() , impl_0), this$static.focusable);
  }
   else {
    $blur_0(($clinit_66() , impl_0), this$static.focusable);
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
  for (it = $iterator(this); $hasNext_2(it);) {
    child = $next_1(it);
    child.onAttach();
  }
  setEventListener(this.focusable, this);
}

function doDetachChildren_0(){
  var child, it;
  for (it = $iterator(this); $hasNext_2(it);) {
    child = $next_1(it);
    child.onDetach();
  }
  setEventListener(this.focusable, null);
}

function iterator_2(){
  return $iterator(this);
}

function onBrowserEvent_6(event_0){
  var chain, e, eventType, item, parent;
  eventType = eventGetType(event_0);
  switch (eventType) {
    case 1:
      {
        e = eventGetTarget(event_0);
        if ($shouldTreeDelegateFocusToElement(this, e)) {
        }
         else {
          $setFocus(this, true);
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

function remove_8(w){
  var item;
  item = dynamicCast($get_1(this.childWidgets, w), 16);
  if (item === null) {
    return false;
  }
  $setWidget_3(item, null);
  return true;
}

function Tree(){
}

_ = Tree.prototype = new Widget();
_.doAttachChildren = doAttachChildren_0;
_.doDetachChildren = doDetachChildren_0;
_.iterator_0 = iterator_2;
_.onBrowserEvent = onBrowserEvent_6;
_.onLoad = onLoad_2;
_.remove_0 = remove_8;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'Tree';
_.typeId$ = 88;
_.curSelection = null;
_.focusable = null;
_.images = null;
_.lastEventType = 0;
_.root = null;
function $$init_14(this$static){
  this$static.children_0 = $ArrayList(new ArrayList());
  this$static.statusImage = $Image(new Image_0());
}

function $TreeItem(this$static){
  var tbody, tdContent, tdImg, tr;
  $$init_14(this$static);
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
  $setHTML_1(this$static, html);
  return this$static;
}

function $getChild_0(this$static, index){
  if (index < 0 || index >= this$static.children_0.size) {
    return null;
  }
  return dynamicCast($get_0(this$static.children_0, index), 16);
}

function $getChildIndex_0(this$static, child){
  return $indexOf_2(this$static.children_0, child);
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

function $remove_4(this$static){
  if (this$static.parent !== null) {
    this$static.parent.removeItem(this$static);
  }
   else if (this$static.tree !== null) {
    $removeItem_0(this$static.tree, this$static);
  }
}

function $setHTML_1(this$static, html){
  $setWidget_3(this$static, null);
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
    $setTree(dynamicCast($get_0(this$static.children_0, i), 16), newTree);
  }
  $updateState(this$static);
}

function $setWidget_3(this$static, newWidget){
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
    $applyTo(($clinit_20() , treeLeaf_SINGLETON), this$static.statusImage);
    return;
  }
  if (this$static.open) {
    setVisible_0(this$static.childSpanElem, true);
    $applyTo(($clinit_20() , treeOpen_SINGLETON), this$static.statusImage);
  }
   else {
    setVisible_0(this$static.childSpanElem, false);
    $applyTo(($clinit_20() , treeClosed_SINGLETON), this$static.statusImage);
  }
}

function $updateStateRecursive(this$static){
  var i, n;
  $updateState(this$static);
  for (i = 0 , n = this$static.children_0.size; i < n; ++i) {
    $updateStateRecursive(dynamicCast($get_0(this$static.children_0, i), 16));
  }
}

function addItem_0(item){
  if (item.parent !== null || item.tree !== null) {
    $remove_4(item);
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
  if (!$contains(this.children_0, item)) {
    return;
  }
  $setTree(item, null);
  removeChild(this.childSpanElem, item.getElement());
  $setParentItem(item, null);
  $remove_9(this.children_0, item);
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
_.typeId$ = 89;
_.childSpanElem = null;
_.contentElem = null;
_.itemTable = null;
_.open = false;
_.parent = null;
_.selected = false;
_.tree = null;
_.widget = null;
function $Tree$1(this$static, this$0){
  this$static.this$0 = this$0;
  $TreeItem(this$static);
  return this$static;
}

function $addItem(this$static, item){
  if (item.parent !== null || item.tree !== null) {
    $remove_4(item);
  }
  appendChild(this$static.this$0.getElement(), item.getElement());
  $setTree(item, this$static.tree);
  $setParentItem(item, null);
  $add_9(this$static.children_0, item);
  setIntStyleAttribute(item.getElement(), 'marginLeft', 0);
}

function $removeItem(this$static, item){
  if (!$contains(this$static.children_0, item)) {
    return;
  }
  $setTree(item, null);
  $setParentItem(item, null);
  $remove_9(this$static.children_0, item);
  removeChild(this$static.this$0.getElement(), item.getElement());
}

function addItem(item){
  $addItem(this, item);
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
_.typeId$ = 90;
function $$init_16(this$static){
  this$static.horzAlign = ($clinit_80() , ALIGN_LEFT);
  this$static.vertAlign = ($clinit_84() , ALIGN_TOP);
}

function $VerticalPanel(this$static){
  $CellPanel(this$static);
  $$init_16(this$static);
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
  $add_1(this$static, w, td);
}

function $createAlignedTd_0(this$static){
  var td;
  td = createTD();
  $setCellHorizontalAlignment(this$static, td, this$static.horzAlign);
  $setCellVerticalAlignment(this$static, td, this$static.vertAlign);
  return td;
}

function $setHorizontalAlignment_1(this$static, align){
  this$static.horzAlign = align;
}

function remove_9(w){
  var removed, td;
  td = getParent(w.getElement());
  removed = $remove_1(this, w);
  if (removed) {
    removeChild(this.body_0, getParent(td));
  }
  return removed;
}

function VerticalPanel(){
}

_ = VerticalPanel.prototype = new CellPanel();
_.remove_0 = remove_9;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'VerticalPanel';
_.typeId$ = 91;
function $WidgetCollection(this$static, parent){
  this$static.array = initDims_0('[Lcom.google.gwt.user.client.ui.Widget;', [125], [12], [4], null);
  return this$static;
}

function $add_8(this$static, w){
  $insert_1(this$static, w, this$static.size);
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

function $insert_1(this$static, w, beforeIndex){
  var i, newArray;
  if (beforeIndex < 0 || beforeIndex > this$static.size) {
    throw new IndexOutOfBoundsException();
  }
  if (this$static.size == this$static.array.length_0) {
    newArray = initDims_0('[Lcom.google.gwt.user.client.ui.Widget;', [125], [12], [this$static.array.length_0 * 2], null);
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

function $iterator_0(this$static){
  return $WidgetCollection$WidgetIterator(new WidgetCollection$WidgetIterator(), this$static);
}

function $remove_5(this$static, index){
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

function $remove_6(this$static, w){
  var index;
  index = $indexOf(this$static, w);
  if (index == (-1)) {
    throw new NoSuchElementException();
  }
  $remove_5(this$static, index);
}

function WidgetCollection(){
}

_ = WidgetCollection.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'WidgetCollection';
_.typeId$ = 92;
_.array = null;
_.size = 0;
function $WidgetCollection$WidgetIterator(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $hasNext_1(this$static){
  return this$static.index < this$static.this$0.size - 1;
}

function $next_0(this$static){
  if (this$static.index >= this$static.this$0.size) {
    throw new NoSuchElementException();
  }
  return this$static.this$0.array[++this$static.index];
}

function hasNext_2(){
  return $hasNext_1(this);
}

function next_3(){
  return $next_0(this);
}

function WidgetCollection$WidgetIterator(){
}

_ = WidgetCollection$WidgetIterator.prototype = new Object_0();
_.hasNext = hasNext_2;
_.next_0 = next_3;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'WidgetCollection$WidgetIterator';
_.typeId$ = 93;
_.index = (-1);
function createWidgetIterator(container, contained){
  return $WidgetIterators$1(new WidgetIterators$1(), contained, container);
}

function $$init_17(this$static){
  {
    $gotoNextIndex(this$static);
  }
}

function $WidgetIterators$1(this$static, val$contained, val$container){
  this$static.val$contained = val$contained;
  $$init_17(this$static);
  return this$static;
}

function $gotoNextIndex(this$static){
  ++this$static.index;
  while (this$static.index < this$static.val$contained.length_0) {
    if (this$static.val$contained[this$static.index] !== null) {
      return;
    }
    ++this$static.index;
  }
}

function $hasNext_2(this$static){
  return this$static.index < this$static.val$contained.length_0;
}

function $next_1(this$static){
  var w;
  if (!$hasNext_2(this$static)) {
    throw new NoSuchElementException();
  }
  w = this$static.val$contained[this$static.index];
  $gotoNextIndex(this$static);
  return w;
}

function hasNext_3(){
  return $hasNext_2(this);
}

function next_4(){
  return $next_1(this);
}

function WidgetIterators$1(){
}

_ = WidgetIterators$1.prototype = new Object_0();
_.hasNext = hasNext_3;
_.next_0 = next_4;
_.typeName$ = package_com_google_gwt_user_client_ui_ + 'WidgetIterators$1';
_.typeId$ = 94;
_.index = (-1);
function $createStructure(this$static, url, left, top, width, height){
  var tmp;
  tmp = createSpan();
  setInnerHTML(tmp, $getHTML(this$static, url, left, top, width, height));
  return getFirstChild(tmp);
}

function ClippedImageImpl(){
}

_ = ClippedImageImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'ClippedImageImpl';
_.typeId$ = 95;
function $clinit_130(){
  $clinit_130 = nullMethod;
  moduleBaseUrlProtocol = $startsWith(getHostPageBaseURL(), 'https')?'https://':'http://';
}

function $ClippedImageImplIE6(this$static){
  $clinit_130();
  injectGlobalHandler();
  return this$static;
}

function $adjust(this$static, clipper, url, left, top, width, height){
  var img, imgHeight, imgWidth;
  setStyleAttribute(clipper, 'width', width + 'px');
  setStyleAttribute(clipper, 'height', height + 'px');
  img = getFirstChild(clipper);
  setStyleAttribute(img, 'filter', "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + url + "',sizingMethod='crop')");
  setStyleAttribute(img, 'marginLeft', -left + 'px');
  setStyleAttribute(img, 'marginTop', -top + 'px');
  imgWidth = left + width;
  imgHeight = top + height;
  setElementPropertyInt(img, 'width', imgWidth);
  setElementPropertyInt(img, 'height', imgHeight);
}

function $getHTML(this$static, url, left, top, width, height){
  var clippedImgHtml, clipperStyle, imgStyle;
  clipperStyle = 'overflow: hidden; width: ' + width + 'px; height: ' + height + 'px; padding: 0px; zoom: 1';
  imgStyle = "filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + url + "',sizingMethod='crop'); margin-left: " + -left + 'px; margin-top: ' + -top + 'px; border: none';
  clippedImgHtml = '<gwt:clipper style="' + clipperStyle + '"><img src=\'' + moduleBaseUrlProtocol + "' onerror='if(window.__gwt_transparentImgHandler)window.__gwt_transparentImgHandler(this);else this.src=\"" + getModuleBaseURL() + 'clear.cache.gif"\' style="' + imgStyle + '" width=' + (left + width) + ' height=' + (top + height) + " border='0'><\/gwt:clipper>";
  return clippedImgHtml;
}

function injectGlobalHandler(){
  $clinit_130();
  $wnd.__gwt_transparentImgHandler = function(elem){
    elem.onerror = null;
    setImgSrc(elem, getModuleBaseURL() + 'clear.cache.gif');
  }
  ;
}

function ClippedImageImplIE6(){
}

_ = ClippedImageImplIE6.prototype = new ClippedImageImpl();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'ClippedImageImplIE6';
_.typeId$ = 96;
var moduleBaseUrlProtocol;
function $clinit_132(){
  $clinit_132 = nullMethod;
  impl_3 = $ClippedImageImplIE6(new ClippedImageImplIE6());
}

function $ClippedImagePrototype(this$static, url, left, top, width, height){
  $clinit_132();
  this$static.url = url;
  this$static.left_0 = left;
  this$static.top_0 = top;
  this$static.width_0 = width;
  this$static.height_0 = height;
  return this$static;
}

function $applyTo(this$static, image){
  $setUrlAndVisibleRect(image, this$static.url, this$static.left_0, this$static.top_0, this$static.width_0, this$static.height_0);
}

function $createImage(this$static){
  return $Image_0(new Image_0(), this$static.url, this$static.left_0, this$static.top_0, this$static.width_0, this$static.height_0);
}

function $getHTML_0(this$static){
  return $getHTML(impl_3, this$static.url, this$static.left_0, this$static.top_0, this$static.width_0, this$static.height_0);
}

function ClippedImagePrototype(){
}

_ = ClippedImagePrototype.prototype = new AbstractImagePrototype();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'ClippedImagePrototype';
_.typeId$ = 97;
_.height_0 = 0;
_.left_0 = 0;
_.top_0 = 0;
_.url = null;
_.width_0 = 0;
var impl_3;
function $clinit_134(){
  $clinit_134 = nullMethod;
  implPanel = $FocusImplIE6(new FocusImplIE6());
  implWidget = implPanel;
}

function $FocusImpl(this$static){
  $clinit_134();
  return this$static;
}

function $blur_0(this$static, elem){
  elem.blur();
}

function $createFocusable(this$static){
  var e = $doc.createElement('DIV');
  e.tabIndex = 0;
  return e;
}

function FocusImpl(){
}

_ = FocusImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'FocusImpl';
_.typeId$ = 98;
var implPanel, implWidget;
function $clinit_133(){
  $clinit_133 = nullMethod;
  $clinit_134();
}

function $FocusImplIE6(this$static){
  $clinit_133();
  $FocusImpl(this$static);
  return this$static;
}

function $focus(this$static, elem){
  try {
    elem.focus();
  }
   catch (e) {
    if (!elem || !elem.focus) {
      throw e;
    }
  }
}

function FocusImplIE6(){
}

_ = FocusImplIE6.prototype = new FocusImpl();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'FocusImplIE6';
_.typeId$ = 99;
function $createElement_0(this$static){
  return createDiv();
}

function PopupImpl(){
}

_ = PopupImpl.prototype = new Object_0();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'PopupImpl';
_.typeId$ = 100;
function $onHide(this$static, popup){
  var frame = popup.__frame;
  frame.parentElement.removeChild(frame);
  popup.__frame = null;
  frame.__popup = null;
}

function $onShow(this$static, popup){
  var frame = $doc.createElement('iframe');
  frame.src = "javascript:''";
  frame.scrolling = 'no';
  frame.frameBorder = 0;
  popup.__frame = frame;
  frame.__popup = popup;
  var style = frame.style;
  style.position = 'absolute';
  style.filter = 'alpha(opacity=0)';
  style.visibility = popup.style.visibility;
  style.left = popup.offsetLeft;
  style.top = popup.offsetTop;
  style.width = popup.offsetWidth;
  style.height = popup.offsetHeight;
  style.setExpression('left', 'this.__popup.offsetLeft');
  style.setExpression('top', 'this.__popup.offsetTop');
  style.setExpression('width', 'this.__popup.offsetWidth');
  style.setExpression('height', 'this.__popup.offsetHeight');
  popup.parentElement.insertBefore(frame, popup);
}

function $setVisible_0(this$static, popup, visible){
  if (popup.__frame) {
    popup.__frame.style.visibility = visible?'visible':'hidden';
  }
}

function PopupImplIE6(){
}

_ = PopupImplIE6.prototype = new PopupImpl();
_.typeName$ = package_com_google_gwt_user_client_ui_impl_ + 'PopupImplIE6';
_.typeId$ = 101;
function ArrayStoreException(){
}

_ = ArrayStoreException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'ArrayStoreException';
_.typeId$ = 102;
function digit(c, radix){
  if (radix < 2 || radix > 36) {
    return (-1);
  }
  if (c >= 48 && c < 48 + min(radix, 10)) {
    return c - 48;
  }
  if (c >= 97 && c < radix + 97 - 10) {
    return c - 97 + 10;
  }
  if (c >= 65 && c < radix + 65 - 10) {
    return c - 65 + 10;
  }
  return (-1);
}

function ClassCastException(){
}

_ = ClassCastException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'ClassCastException';
_.typeId$ = 103;
function $IllegalArgumentException(this$static, message){
  $RuntimeException(this$static, message);
  return this$static;
}

function IllegalArgumentException(){
}

_ = IllegalArgumentException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'IllegalArgumentException';
_.typeId$ = 104;
function $IllegalStateException(this$static, s){
  $RuntimeException(this$static, s);
  return this$static;
}

function IllegalStateException(){
}

_ = IllegalStateException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'IllegalStateException';
_.typeId$ = 105;
function $IndexOutOfBoundsException(this$static, message){
  $RuntimeException(this$static, message);
  return this$static;
}

function IndexOutOfBoundsException(){
}

_ = IndexOutOfBoundsException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'IndexOutOfBoundsException';
_.typeId$ = 106;
function $clinit_152(){
  $clinit_152 = nullMethod;
  {
    initNative();
  }
}

function __isLongNaN(x){
  $clinit_152();
  return isNaN(x);
}

function __parseAndValidateLong(s, radix, lowerBound, upperBound){
  $clinit_152();
  var i, length, startIndex, toReturn;
  if (s === null) {
    throw $NumberFormatException(new NumberFormatException(), 'Unable to parse null');
  }
  length = $length(s);
  startIndex = length > 0 && $charAt(s, 0) == 45?1:0;
  for (i = startIndex; i < length; i++) {
    if (digit($charAt(s, i), radix) == (-1)) {
      throw $NumberFormatException(new NumberFormatException(), 'Could not parse ' + s + ' in radix ' + radix);
    }
  }
  toReturn = __parseInt(s, radix);
  if (__isLongNaN(toReturn)) {
    throw $NumberFormatException(new NumberFormatException(), 'Unable to parse ' + s);
  }
   else if (toReturn < lowerBound || toReturn > upperBound) {
    throw $NumberFormatException(new NumberFormatException(), 'The string ' + s + ' exceeds the range for the requested data type');
  }
  return toReturn;
}

function __parseInt(s, radix){
  $clinit_152();
  return parseInt(s, radix);
}

function initNative(){
  $clinit_152();
  floatRegex = /^[+-]?\d*\.?\d*(e[+-]?\d+)?$/i;
}

var floatRegex = null;
function $clinit_147(){
  $clinit_147 = nullMethod;
  $clinit_152();
}

function parseInt_0(s){
  $clinit_147();
  return parseInt_1(s, 10);
}

function parseInt_1(s, radix){
  $clinit_147();
  return narrow_int(__parseAndValidateLong(s, radix, (-2147483648), 2147483647));
}

var MAX_VALUE = 2147483647, MIN_VALUE = (-2147483648);
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
_.typeId$ = 107;
function $NullPointerException(this$static, message){
  $RuntimeException(this$static, message);
  return this$static;
}

function NullPointerException(){
}

_ = NullPointerException.prototype = new RuntimeException();
_.typeName$ = package_java_lang_ + 'NullPointerException';
_.typeId$ = 108;
function $NumberFormatException(this$static, message){
  $IllegalArgumentException(this$static, message);
  return this$static;
}

function NumberFormatException(){
}

_ = NumberFormatException.prototype = new IllegalArgumentException();
_.typeName$ = package_java_lang_ + 'NumberFormatException';
_.typeId$ = 109;
function $charAt(this$static, index){
  return this$static.charCodeAt(index);
}

function $equals_1(this$static, other){
  if (!instanceOf(other, 1))
    return false;
  return __equals(this$static, other);
}

function $equalsIgnoreCase(this$static, other){
  if (other == null)
    return false;
  return this$static == other || this$static.toLowerCase() == other.toLowerCase();
}

function $indexOf_0(this$static, str){
  return this$static.indexOf(str);
}

function $indexOf_1(this$static, str, startIndex){
  return this$static.indexOf(str, startIndex);
}

function $length(this$static){
  return this$static.length;
}

function $startsWith(this$static, prefix){
  return $indexOf_0(this$static, prefix) == 0;
}

function $substring(this$static, beginIndex){
  return this$static.substr(beginIndex, this$static.length - beginIndex);
}

function $substring_0(this$static, beginIndex, endIndex){
  return this$static.substr(beginIndex, endIndex - beginIndex);
}

function $trim(this$static){
  var r1 = this$static.replace(/^(\s*)/, '');
  var r2 = r1.replace(/\s*$/, '');
  return r2;
}

function __equals(me, other){
  return String(me) == other;
}

function equals_3(other){
  return $equals_1(this, other);
}

function hashCode_4(){
  var hashCache = hashCache_0;
  if (!hashCache) {
    hashCache = hashCache_0 = {};
  }
  var key = ':' + this;
  var hashCode = hashCache[key];
  if (hashCode == null) {
    hashCode = 0;
    var n = this.length;
    var inc = n < 64?1:n / 32 | 0;
    for (var i = 0; i < n; i += inc) {
      hashCode <<= 1;
      hashCode += this.charCodeAt(i);
    }
    hashCode |= 0;
    hashCache[key] = hashCode;
  }
  return hashCode;
}

_ = String.prototype;
_.equals$ = equals_3;
_.hashCode$ = hashCode_4;
_.typeName$ = package_java_lang_ + 'String';
_.typeId$ = 2;
var hashCache_0 = null;
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
_.typeId$ = 111;
function $AbstractList$IteratorImpl(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $hasNext_3(this$static){
  return this$static.i < this$static.this$0.size_0();
}

function $next_2(this$static){
  if (!$hasNext_3(this$static)) {
    throw new NoSuchElementException();
  }
  return this$static.this$0.get(this$static.last = this$static.i++);
}

function $remove_7(this$static){
  if (this$static.last < 0) {
    throw new IllegalStateException();
  }
  this$static.this$0.remove(this$static.last);
  this$static.i = this$static.last;
  this$static.last = (-1);
}

function hasNext_4(){
  return $hasNext_3(this);
}

function next_5(){
  return $next_2(this);
}

function AbstractList$IteratorImpl(){
}

_ = AbstractList$IteratorImpl.prototype = new Object_0();
_.hasNext = hasNext_4;
_.next_0 = next_5;
_.typeName$ = package_java_util_ + 'AbstractList$IteratorImpl';
_.typeId$ = 112;
_.i = 0;
_.last = (-1);
function $implFindEntry(this$static, key, remove){
  var entry, iter, k;
  for (iter = $iterator_4(this$static.entrySet()); $hasNext_6(iter);) {
    entry = $next_5(iter);
    k = entry.getKey();
    if (key === null?k === null:key.equals$(k)) {
      if (remove) {
        $remove_10(iter);
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

function containsKey(key){
  return $implFindEntry(this, key, false) !== null;
}

function equals_5(obj){
  var iter, key, keys, otherKeys, otherMap, otherValue, value;
  if (obj === this) {
    return true;
  }
  if (!instanceOf(obj, 26)) {
    return false;
  }
  otherMap = dynamicCast(obj, 26);
  keys = $keySet(this);
  otherKeys = otherMap.keySet();
  if (!$equals_2(keys, otherKeys)) {
    return false;
  }
  for (iter = $iterator_2(keys); $hasNext_4(iter);) {
    key = $next_3(iter);
    value = this.get_0(key);
    otherValue = otherMap.get_0(key);
    if (value === null?otherValue !== null:!value.equals$(otherValue)) {
      return false;
    }
  }
  return true;
}

function get_1(key){
  var entry;
  entry = $implFindEntry(this, key, false);
  return entry === null?null:entry.getValue();
}

function hashCode_6(){
  var entry, hashCode, iter;
  hashCode = 0;
  for (iter = $iterator_4(this.entrySet()); $hasNext_6(iter);) {
    entry = $next_5(iter);
    hashCode += entry.hashCode$();
  }
  return hashCode;
}

function keySet(){
  return $keySet(this);
}

function AbstractMap(){
}

_ = AbstractMap.prototype = new Object_0();
_.containsKey = containsKey;
_.equals$ = equals_5;
_.get_0 = get_1;
_.hashCode$ = hashCode_6;
_.keySet = keySet;
_.typeName$ = package_java_util_ + 'AbstractMap';
_.typeId$ = 113;
function $equals_2(this$static, o){
  var iter, other, otherItem;
  if (o === this$static) {
    return true;
  }
  if (!instanceOf(o, 27)) {
    return false;
  }
  other = dynamicCast(o, 27);
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

function equals_6(o){
  return $equals_2(this, o);
}

function hashCode_7(){
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
_.equals$ = equals_6;
_.hashCode$ = hashCode_7;
_.typeName$ = package_java_util_ + 'AbstractSet';
_.typeId$ = 114;
function $AbstractMap$1(this$static, this$0, val$entrySet){
  this$static.this$0 = this$0;
  this$static.val$entrySet = val$entrySet;
  return this$static;
}

function $iterator_2(this$static){
  var outerIter;
  outerIter = $iterator_4(this$static.val$entrySet);
  return $AbstractMap$2(new AbstractMap$2(), this$static, outerIter);
}

function contains_0(key){
  return this.this$0.containsKey(key);
}

function iterator_4(){
  return $iterator_2(this);
}

function size_0(){
  return this.val$entrySet.this$0.size;
}

function AbstractMap$1(){
}

_ = AbstractMap$1.prototype = new AbstractSet();
_.contains = contains_0;
_.iterator_0 = iterator_4;
_.size_0 = size_0;
_.typeName$ = package_java_util_ + 'AbstractMap$1';
_.typeId$ = 115;
function $AbstractMap$2(this$static, this$1, val$outerIter){
  this$static.val$outerIter = val$outerIter;
  return this$static;
}

function $hasNext_4(this$static){
  return this$static.val$outerIter.hasNext();
}

function $next_3(this$static){
  var entry;
  entry = this$static.val$outerIter.next_0();
  return entry.getKey();
}

function hasNext_5(){
  return $hasNext_4(this);
}

function next_6(){
  return $next_3(this);
}

function AbstractMap$2(){
}

_ = AbstractMap$2.prototype = new Object_0();
_.hasNext = hasNext_5;
_.next_0 = next_6;
_.typeName$ = package_java_util_ + 'AbstractMap$2';
_.typeId$ = 116;
function $AbstractMap$3(this$static, this$0, val$entrySet){
  this$static.this$0 = this$0;
  this$static.val$entrySet = val$entrySet;
  return this$static;
}

function $iterator_3(this$static){
  var outerIter;
  outerIter = $iterator_4(this$static.val$entrySet);
  return $AbstractMap$4(new AbstractMap$4(), this$static, outerIter);
}

function contains_1(value){
  return $containsValue(this.this$0, value);
}

function iterator_5(){
  return $iterator_3(this);
}

function size_1(){
  return this.val$entrySet.this$0.size;
}

function AbstractMap$3(){
}

_ = AbstractMap$3.prototype = new AbstractCollection();
_.contains = contains_1;
_.iterator_0 = iterator_5;
_.size_0 = size_1;
_.typeName$ = package_java_util_ + 'AbstractMap$3';
_.typeId$ = 117;
function $AbstractMap$4(this$static, this$1, val$outerIter){
  this$static.val$outerIter = val$outerIter;
  return this$static;
}

function $hasNext_5(this$static){
  return this$static.val$outerIter.hasNext();
}

function $next_4(this$static){
  var value;
  value = this$static.val$outerIter.next_0().getValue();
  return value;
}

function hasNext_6(){
  return $hasNext_5(this);
}

function next_7(){
  return $next_4(this);
}

function AbstractMap$4(){
}

_ = AbstractMap$4.prototype = new Object_0();
_.hasNext = hasNext_6;
_.next_0 = next_7;
_.typeName$ = package_java_util_ + 'AbstractMap$4';
_.typeId$ = 118;
function $clinit_174(){
  $clinit_174 = nullMethod;
  UNDEFINED = createUndefinedValue();
}

function $$init_19(this$static){
  {
    $clearImpl_0(this$static);
  }
}

function $HashMap(this$static){
  $clinit_174();
  $$init_19(this$static);
  return this$static;
}

function $clearImpl_0(this$static){
  this$static.hashCodeMap = createArray();
  this$static.stringMap = createObject();
  this$static.nullSlot = wrapJSO(UNDEFINED, JavaScriptObject);
  this$static.size = 0;
}

function $containsKey(this$static, key){
  if (instanceOf(key, 1)) {
    return getStringValue(this$static.stringMap, dynamicCast(key, 1)) !== UNDEFINED;
  }
   else if (key === null) {
    return this$static.nullSlot !== UNDEFINED;
  }
   else {
    return getHashValue(this$static.hashCodeMap, key, key.hashCode$()) !== UNDEFINED;
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

function $get_1(this$static, key){
  var result;
  if (instanceOf(key, 1)) {
    result = getStringValue(this$static.stringMap, dynamicCast(key, 1));
  }
   else if (key === null) {
    result = this$static.nullSlot;
  }
   else {
    result = getHashValue(this$static.hashCodeMap, key, key.hashCode$());
  }
  return result === UNDEFINED?null:result;
}

function $put(this$static, key, value){
  var previous;
  {
    previous = this$static.nullSlot;
    this$static.nullSlot = value;
  }
  if (previous === UNDEFINED) {
    ++this$static.size;
    return null;
  }
   else {
    return previous;
  }
}

function $remove_11(this$static, key){
  var previous;
  if (instanceOf(key, 1)) {
    previous = removeStringValue(this$static.stringMap, dynamicCast(key, 1));
  }
   else if (key === null) {
    previous = this$static.nullSlot;
    this$static.nullSlot = wrapJSO(UNDEFINED, JavaScriptObject);
  }
   else {
    previous = removeHashValue(this$static.hashCodeMap, key, key.hashCode$());
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
  $clinit_174();
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
  $clinit_174();
  for (var key in stringMap) {
    if (key.charCodeAt(0) == 58) {
      var value = stringMap[key];
      var entry = create(key.substring(1), value);
      dest.add_0(entry);
    }
  }
}

function containsHashValue(hashCodeMap, value){
  $clinit_174();
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

function containsKey_0(key){
  return $containsKey(this, key);
}

function containsStringValue(stringMap, value){
  $clinit_174();
  for (var key in stringMap) {
    if (key.charCodeAt(0) == 58) {
      var entryValue = stringMap[key];
      if (equalsWithNullCheck(value, entryValue)) {
        return true;
      }
    }
  }
  return false;
}

function createUndefinedValue(){
  $clinit_174();
}

function entrySet_0(){
  return $entrySet(this);
}

function equalsWithNullCheck(a, b){
  $clinit_174();
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

function get_3(key){
  return $get_1(this, key);
}

function getHashValue(hashCodeMap, key, hashCode){
  $clinit_174();
  var array = hashCodeMap[hashCode];
  if (array) {
    for (var i = 0, c = array.length; i < c; ++i) {
      var entry = array[i];
      var entryKey = entry.getKey();
      if (equalsWithNullCheck(key, entryKey)) {
        return entry.getValue();
      }
    }
  }
}

function getStringValue(stringMap, key){
  $clinit_174();
  return stringMap[':' + key];
}

function removeHashValue(hashCodeMap, key, hashCode){
  $clinit_174();
  var array = hashCodeMap[hashCode];
  if (array) {
    for (var i = 0, c = array.length; i < c; ++i) {
      var entry = array[i];
      var entryKey = entry.getKey();
      if (equalsWithNullCheck(key, entryKey)) {
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

function removeStringValue(stringMap, key){
  $clinit_174();
  key = ':' + key;
  var result = stringMap[key];
  delete stringMap[key];
  return result;
}

function HashMap(){
}

_ = HashMap.prototype = new AbstractMap();
_.containsKey = containsKey_0;
_.entrySet = entrySet_0;
_.get_0 = get_3;
_.typeName$ = package_java_util_ + 'HashMap';
_.typeId$ = 119;
_.hashCodeMap = null;
_.nullSlot = null;
_.size = 0;
_.stringMap = null;
var UNDEFINED;
function $HashMap$EntryImpl(this$static, key, value){
  this$static.key = key;
  this$static.value = value;
  return this$static;
}

function create(key, value){
  return $HashMap$EntryImpl(new HashMap$EntryImpl(), key, value);
}

function equals_8(other){
  var entry;
  if (instanceOf(other, 28)) {
    entry = dynamicCast(other, 28);
    if (equalsWithNullCheck(this.key, entry.getKey()) && equalsWithNullCheck(this.value, entry.getValue())) {
      return true;
    }
  }
  return false;
}

function getKey(){
  return this.key;
}

function getValue_0(){
  return this.value;
}

function hashCode_8(){
  var keyHash, valueHash;
  keyHash = 0;
  valueHash = 0;
  if (this.key !== null) {
    keyHash = this.key.hashCode$();
  }
  if (this.value !== null) {
    valueHash = this.value.hashCode$();
  }
  return keyHash ^ valueHash;
}

function HashMap$EntryImpl(){
}

_ = HashMap$EntryImpl.prototype = new Object_0();
_.equals$ = equals_8;
_.getKey = getKey;
_.getValue = getValue_0;
_.hashCode$ = hashCode_8;
_.typeName$ = package_java_util_ + 'HashMap$EntryImpl';
_.typeId$ = 120;
_.key = null;
_.value = null;
function $HashMap$EntrySet(this$static, this$0){
  this$static.this$0 = this$0;
  return this$static;
}

function $iterator_4(this$static){
  return $HashMap$EntrySetIterator(new HashMap$EntrySetIterator(), this$static.this$0);
}

function contains_3(o){
  var entry, key, value;
  if (instanceOf(o, 28)) {
    entry = dynamicCast(o, 28);
    key = entry.getKey();
    if ($containsKey(this.this$0, key)) {
      value = $get_1(this.this$0, key);
      return equalsWithNullCheck(entry.getValue(), value);
    }
  }
  return false;
}

function iterator_6(){
  return $iterator_4(this);
}

function size_3(){
  return this.this$0.size;
}

function HashMap$EntrySet(){
}

_ = HashMap$EntrySet.prototype = new AbstractSet();
_.contains = contains_3;
_.iterator_0 = iterator_6;
_.size_0 = size_3;
_.typeName$ = package_java_util_ + 'HashMap$EntrySet';
_.typeId$ = 121;
function $HashMap$EntrySetIterator(this$static, this$0){
  var list;
  this$static.this$0 = this$0;
  list = $ArrayList(new ArrayList());
  if (this$static.this$0.nullSlot !== ($clinit_174() , UNDEFINED)) {
    $add_9(list, $HashMap$EntryImpl(new HashMap$EntryImpl(), null, this$static.this$0.nullSlot));
  }
  addAllStringEntries(this$static.this$0.stringMap, list);
  addAllHashEntries(this$static.this$0.hashCodeMap, list);
  this$static.iter = $iterator_1(list);
  return this$static;
}

function $hasNext_6(this$static){
  return $hasNext_3(this$static.iter);
}

function $next_5(this$static){
  return this$static.last = dynamicCast($next_2(this$static.iter), 28);
}

function $remove_10(this$static){
  if (this$static.last === null) {
    throw $IllegalStateException(new IllegalStateException(), 'Must call next() before remove().');
  }
   else {
    $remove_7(this$static.iter);
    $remove_11(this$static.this$0, this$static.last.getKey());
    this$static.last = null;
  }
}

function hasNext_7(){
  return $hasNext_6(this);
}

function next_8(){
  return $next_5(this);
}

function HashMap$EntrySetIterator(){
}

_ = HashMap$EntrySetIterator.prototype = new Object_0();
_.hasNext = hasNext_7;
_.next_0 = next_8;
_.typeName$ = package_java_util_ + 'HashMap$EntrySetIterator';
_.typeId$ = 122;
_.iter = null;
_.last = null;
function NoSuchElementException(){
}

_ = NoSuchElementException.prototype = new RuntimeException();
_.typeName$ = package_java_util_ + 'NoSuchElementException';
_.typeId$ = 123;
function init_0(){
  $onModuleLoad($Mail(new Mail()));
}

function gwtOnLoad(errFn, modName, modBase){
  $moduleName = modName;
  $moduleBase = modBase;
  if (errFn)
    try {
      init_0();
    }
     catch (e) {
      errFn(modName);
    }
   else {
    init_0();
  }
}

var typeIdArray = [{}, {18:1}, {1:1, 18:1, 23:1, 24:1}, {3:1, 18:1}, {3:1, 18:1}, {3:1, 18:1}, {3:1, 18:1}, {2:1, 18:1}, {18:1}, {18:1}, {18:1}, {18:1, 19:1}, {12:1, 18:1, 19:1, 20:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {6:1, 12:1, 17:1, 18:1, 19:1, 20:1}, {6:1, 12:1, 13:1, 17:1, 18:1, 19:1, 20:1}, {6:1, 12:1, 13:1, 17:1, 18:1, 19:1, 20:1}, {11:1, 18:1}, {12:1, 18:1, 19:1, 20:1}, {12:1, 18:1, 19:1, 20:1}, {11:1, 18:1}, {18:1, 21:1}, {6:1, 12:1, 17:1, 18:1, 19:1, 20:1}, {10:1, 18:1}, {5:1, 18:1}, {12:1, 18:1, 19:1, 20:1}, {4:1, 18:1}, {11:1, 12:1, 15:1, 18:1, 19:1, 20:1}, {18:1}, {12:1, 18:1, 19:1, 20:1}, {12:1, 18:1, 19:1, 20:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {12:1, 18:1, 19:1, 20:1}, {11:1, 12:1, 18:1, 19:1, 20:1}, {3:1, 18:1}, {18:1}, {8:1, 18:1}, {8:1, 18:1}, {8:1, 18:1}, {18:1}, {2:1, 7:1, 18:1}, {2:1, 18:1}, {9:1, 18:1}, {18:1}, {18:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {18:1}, {12:1, 18:1, 19:1, 20:1}, {12:1, 18:1, 19:1, 20:1}, {12:1, 18:1, 19:1, 20:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {12:1, 18:1, 19:1, 20:1}, {18:1}, {18:1, 25:1}, {18:1, 25:1}, {18:1, 25:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {18:1}, {18:1}, {18:1, 22:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {18:1}, {18:1}, {12:1, 18:1, 19:1, 20:1}, {12:1, 18:1, 19:1, 20:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {12:1, 18:1, 19:1, 20:1}, {5:1, 18:1}, {18:1}, {18:1}, {18:1}, {18:1, 25:1}, {12:1, 14:1, 17:1, 18:1, 19:1, 20:1}, {9:1, 18:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {18:1}, {18:1, 25:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {16:1, 18:1, 19:1}, {16:1, 18:1, 19:1}, {12:1, 17:1, 18:1, 19:1, 20:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {3:1, 18:1}, {3:1, 18:1}, {3:1, 18:1}, {3:1, 18:1}, {3:1, 18:1}, {3:1, 18:1}, {3:1, 18:1}, {3:1, 18:1}, {18:1, 24:1}, {3:1, 18:1}, {18:1}, {18:1, 26:1}, {18:1, 27:1}, {18:1, 27:1}, {18:1}, {18:1}, {18:1}, {18:1, 26:1}, {18:1, 28:1}, {18:1, 27:1}, {18:1}, {3:1, 18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}, {18:1}];

if (com_google_gwt_sample_mail_Mail) {
  var __gwt_initHandlers = com_google_gwt_sample_mail_Mail.__gwt_initHandlers;  com_google_gwt_sample_mail_Mail.onScriptLoad(gwtOnLoad);
}
})();
