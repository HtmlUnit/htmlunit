<?php
# Java.inc -- The PHP/Java Bridge PHP library. Compiled from JavaBridge.inc.
# Copyright (C) 2003-2009 Jost Boekemeier.
# Distributed under the MIT license, see Options.inc for details.
# Customization examples:
# define ("JAVA_HOSTS", 9267); define ("JAVA_SERVLET", false);
# define ("JAVA_HOSTS", "127.0.0.1:8787");
# define ("JAVA_HOSTS", "ssl://my-secure-host.com:8443");
# define ("JAVA_SERVLET", "/MyWebApp/servlet.phpjavabridge");
# define ("JAVA_PREFER_VALUES", 1);

if(!function_exists("java_get_base")) {
1.0E512;
function java_get_base() {
$ar=get_required_files();
$arLen=sizeof($ar);
if($arLen>0) {
$thiz=$ar[$arLen-1];
return dirname($thiz);
} else {
return "java";
}
}
function java_truncate($str) {
if (strlen($str)>955)
return substr($str,0,475).'[...]'.substr($str,-475);
return $str;
}
class java_JavaException extends Exception {
function __toString() {return $this->getMessage();}
};
class java_RuntimeException extends java_JavaException {};
class java_IOException extends java_JavaException {};
class java_ConnectException extends java_IOException {};
class java_IllegalStateException extends java_RuntimeException {};
class java_IllegalArgumentException extends java_RuntimeException {
function __construct($ob) {
parent::__construct("illegal argument: ".gettype($ob));
}
};
function java_autoload_function5($x) {
$s=str_replace("_",".",$x);
$c=__javaproxy_Client_getClient();
if(!($c->invokeMethod(0,"typeExists",array($s)))) return false;
$i="class ${x} extends Java {".
"static function type(\$sub=null){if(\$sub) \$sub='\$'.\$sub; return java('${s}'.\"\$sub\");}".
'function __construct() {$args=func_get_args();'.
'array_unshift($args,'."'$s'".'); parent::__construct($args);}}';
eval ("$i");
return true;
}
function java_autoload_function($x) {
$idx=strrpos($x,"\\"); if (!$idx) return java_autoload_function5($x);
$str=str_replace("\\",".",$x);
$client=__javaproxy_Client_getClient();
if(!($client->invokeMethod(0,"typeExists",array($str)))) return false;
$package=substr($x,0,$idx);
$name=substr($x,1+$idx);
$instance="namespace $package; class ${name} extends \\Java {".
"static function type(\$sub=null){if(\$sub) \$sub='\$'.\$sub;return \\java('${str}'.\"\$sub\");}".
"static function __callStatic(\$procedure,\$args) {return \\java_invoke(\\java('${str}'),\$procedure,\$args);}".
'function __construct() {$args=func_get_args();'.
'array_unshift($args,'."'$str'".'); parent::__construct($args);}}';
eval ("$instance");
return true;
}
if(!defined("JAVA_DISABLE_AUTOLOAD") && function_exists("spl_autoload_register")) spl_autoload_register("java_autoload_function");
function java_autoload($libs=null) {
trigger_error('Please use <a href="http://php-java-bridge.sourceforge.net/pjb/webapp.php>tomcat or jee hot deployment</a> instead',E_USER_WARNING);
}
function java_virtual($path,$return=false) {
$req=java_context()->getHttpServletRequest();
$req=new java("php.java.servlet.VoidInputHttpServletRequest",$req);
$res=java_context()->getHttpServletResponse();
$res=new java("php.java.servlet.RemoteHttpServletResponse",$res);
$req->getRequestDispatcher($path)->include($req,$res);
if ($return) return $res->getBufferContents();
echo $res->getBufferContents();
return true;
}
function Java($name) {
static $classMap=array();
if(array_key_exists($name,$classMap)) return $classMap[$name];
return $classMap[$name]=new JavaClass($name);
}
function java_get_closure() {return java_closure_array(func_get_args());}
function java_wrap() {return java_closure_array(func_get_args());}
function java_get_values($arg) { return java_values($arg); }
function java_get_session() {return java_session_array(func_get_args());}
function java_get_context() {return java_context(); }
function java_get_server_name() { return java_server_name(); }
function java_isnull($value) { return is_null (java_values ($value)); }
function java_is_null($value) { return is_null (java_values ($value)); }
function java_istrue($value) { return (boolean)(java_values ($value)); }
function java_is_true($value) { return (boolean)(java_values ($value)); }
function java_isfalse($value) { return !(java_values ($value)); }
function java_is_false($value) { return !(java_values ($value)); }
function java_set_encoding($enc) { return java_set_file_encoding ($enc); }
function java_call_with_continuation($kontinuation=null) {
if (java_getHeader("X_JAVABRIDGE_INCLUDE",$_SERVER) && !java_getHeader("X_JAVABRIDGE_INCLUDE_ONLY",$_SERVER)) {
if (is_null($kontinuation))
java_context()->call(java_closure());
elseif (is_string($kontinuation))
java_context()->call(call_user_func($kontinuation));
elseif ($kontinuation instanceof java_JavaType)
java_context()->call($kontinuation);
else
java_context()->call(java_closure($kontinuation));
}
}
function java_defineHostFromInitialQuery($java_base) {
if($java_base!="java") {
$url=parse_url($java_base);
if(isset($url["scheme"]) && ($url["scheme"]=="http" || $url["scheme"]=="https")) {
$scheme=$url["scheme"]=="https" ? "ssl://" : "";
$host=$url["host"];
$port=$url["port"];
$path=$url["path"];
define ("JAVA_HOSTS","${scheme}${host}:${port}");
$dir=dirname($path);
define ("JAVA_SERVLET","$dir/servlet.phpjavabridge");
return true;
}
}
return false;
}
define("JAVA_PEAR_VERSION","6.2.1");
if(!defined("JAVA_SEND_SIZE"))
define("JAVA_SEND_SIZE",8192);
if(!defined("JAVA_RECV_SIZE"))
define("JAVA_RECV_SIZE",8192);
if(!defined("JAVA_HOSTS")) {
if(!java_defineHostFromInitialQuery(java_get_base())) {
if ($java_ini=get_cfg_var("java.hosts")) define("JAVA_HOSTS",$java_ini);
else define("JAVA_HOSTS","127.0.0.1:8080");
}
}
if(!defined("JAVA_SERVLET")) {
if (!(($java_ini=get_cfg_var("java.servlet"))===false)) define("JAVA_SERVLET",$java_ini);
else define("JAVA_SERVLET",1);
}
if(!defined("JAVA_LOG_LEVEL"))
if (!(($java_ini=get_cfg_var("java.log_level"))===false)) define("JAVA_LOG_LEVEL",(int)$java_ini);
else define("JAVA_LOG_LEVEL",null);
if (!defined("JAVA_PREFER_VALUES"))
if ($java_ini=get_cfg_var("java.prefer_values")) define("JAVA_PREFER_VALUES",$java_ini);
else define("JAVA_PREFER_VALUES",0);
class java_SimpleFactory {
public $client;
function java_SimpleFactory($client) {
$this->client=$client;
}
function getProxy($result,$signature,$exception,$wrap) {
return $result;
}
function checkResult($result) {
}
}
class java_ProxyFactory extends java_SimpleFactory {
function create($result,$signature) {
return new java_JavaProxy($result,$signature);
}
function createInternal($proxy) {
return new java_InternalJava($proxy);
}
function getProxy($result,$signature,$exception,$wrap) {
$proxy=$this->create($result,$signature);
if($wrap) $proxy=$this->createInternal($proxy);
return $proxy;
}
}
class java_ArrayProxyFactory extends java_ProxyFactory {
function create($result,$signature) {
return new java_ArrayProxy($result,$signature);
}
}
class java_IteratorProxyFactory extends java_ProxyFactory {
function create($result,$signature) {
return new java_IteratorProxy($result,$signature);
}
}
class java_ExceptionProxyFactory extends java_SimpleFactory {
function create($result,$signature) {
return new java_ExceptionProxy($result,$signature);
}
function getProxy($result,$signature,$exception,$wrap) {
$proxy=$this->create($result,$signature);
if($wrap) $proxy=new java_InternalException($proxy,$exception);
return $proxy;
}
}
class java_ThrowExceptionProxyFactory extends java_ExceptionProxyFactory {
function getProxy($result,$signature,$exception,$wrap) {
$proxy=$this->create($result,$signature);
$proxy=new java_InternalException($proxy,$exception);
return $proxy;
}
function checkResult($result) {
if (JAVA_PREFER_VALUES || ($result->__hasDeclaredExceptions=='T'))
throw $result;
else {
trigger_error("Unchecked exception detected: ".java_truncate($result->__toString()),E_USER_WARNING);
}
}
}
class java_CacheEntry {
public $fmt,$signature,$factory,$java;
public $resultVoid;
function java_CacheEntry($fmt,$signature,$factory,$resultVoid) {
$this->fmt=$fmt;
$this->signature=$signature;
$this->factory=$factory;
$this->resultVoid=$resultVoid;
}
}
class java_Arg {
public $client;
public $exception;
public $factory,$val;
public $signature;
function java_Arg($client) {
$this->client=$client;
$this->factory=$client->simpleFactory;
}
function linkResult(&$val) {
$this->val=&$val;
}
function setResult($val) {
$this->val=&$val;
}
function getResult($wrap) {
$rc=$this->factory->getProxy($this->val,$this->signature,$this->exception,$wrap);
$factory=$this->factory;
$this->factory=$this->client->simpleFactory;
$factory->checkResult($rc);
return $rc;
}
function setFactory($factory) {
$this->factory=$factory;
}
function setException($string) {
$this->exception=$string;
}
function setVoidSignature() {
$this->signature="@V";
$key=$this->client->currentCacheKey;
if($key && $key[0]!='~') {
$this->client->currentArgumentsFormat[6]="3";
$cacheEntry=new java_CacheEntry($this->client->currentArgumentsFormat,$this->signature,$this->factory,true);
$this->client->methodCache[$key]=$cacheEntry;
}
}
function setSignature($signature) {
$this->signature=$signature;
$key=$this->client->currentCacheKey;
if($key && $key[0]!='~') {
$cacheEntry=new java_CacheEntry($this->client->currentArgumentsFormat,$this->signature,$this->factory,false);
$this->client->methodCache[$key]=$cacheEntry;
}
}
}
class java_CompositeArg extends java_Arg {
public $parentArg;
public $idx;
public $type;
public $counter;
function java_CompositeArg($client,$type) {
parent::java_Arg($client);
$this->type=$type;
$this->val=array();
$this->counter=0;
}
function setNextIndex() {
$this->idx=$this->counter++;
}
function setIndex($val) {
$this->idx=$val;
}
function linkResult(&$val) {
$this->val[$this->idx]=&$val;
}
function setResult($val) {
$this->val[$this->idx]=$this->factory->getProxy($val,$this->signature,$this->exception,true);
$this->factory=$this->client->simpleFactory;
}
}
class java_ApplyArg extends java_CompositeArg {
public $m,$p,$v,$n;
function java_ApplyArg($client,$type,$m,$p,$v,$n) {
parent::java_CompositeArg($client,$type);
$this->m=$m;
$this->p=$p;
$this->v=$v;
$this->n=$n;
}
}
class java_Client {
public $RUNTIME;
public $result,$exception;
public $parser;
public $simpleArg,$compositeArg;
public $simpleFactory,
$proxyFactory,$iteratorProxyFacroty,
$arrayProxyFactory,$exceptionProxyFactory,$throwExceptionProxyFactory;
public $arg;
public $asyncCtx,$cancelProxyCreationCounter;
public $globalRef;
public $stack;
public $defaultCache=array(),$asyncCache=array(),$methodCache;
public $isAsync=0;
public $currentCacheKey,$currentArgumentsFormat;
public $cachedJavaPrototype;
public $sendBuffer,$preparedToSendBuffer;
public $inArgs;
function java_Client() {
$this->RUNTIME=array();
$this->RUNTIME["NOTICE"]='***USE echo java_inspect(jVal) OR print_r(java_values(jVal)) TO SEE THE CONTENTS OF THIS JAVA OBJECT!***';
$this->parser=new java_Parser($this);
$this->protocol=new java_Protocol($this);
$this->simpleFactory=new java_SimpleFactory($this);
$this->proxyFactory=new java_ProxyFactory($this);
$this->arrayProxyFactory=new java_ArrayProxyFactory($this);
$this->iteratorProxyFactory=new java_IteratorProxyFactory($this);
$this->exceptionProxyFactory=new java_ExceptionProxyFactory($this);
$this->throwExceptionProxyFactory=new java_ThrowExceptionProxyFactory($this);
$this->cachedJavaPrototype=new java_JavaProxyProxy($this);
$this->simpleArg=new java_Arg($this);
$this->globalRef=new java_GlobalRef();
$this->asyncCtx=$this->cancelProxyCreationCounter=0;
$this->methodCache=$this->defaultCache;
$this->inArgs=false;
}
function read($size) {
return $this->protocol->read($size);
}
function setDefaultHandler() {
$this->methodCache=$this->defaultCache;
}
function setAsyncHandler() {
$this->methodCache=$this->asyncCache;
}
function handleRequests() {
$tail_call=false;
do {
$this->stack=array($this->arg=$this->simpleArg);
$this->idx=0;
$this->parser->parse();
if((count($this->stack)) > 1) {
$arg=array_pop($this->stack);
$this->apply($arg);
$tail_call=true;
} else {
$tail_call=false;
}
$this->stack=null;
} while($tail_call);
return 1;
}
function getWrappedResult($wrap) {
return $this->simpleArg->getResult($wrap);
}
function getInternalResult() {
return $this->getWrappedResult(false);
}
function getResult() {
return $this->getWrappedResult(true);
}
function getProxyFactory($type) {
switch($type[0]) {
case 'E':
$factory=$this->exceptionProxyFactory;
break;
case 'C':
$factory=$this->iteratorProxyFactory;
break;
case 'A':
$factory=$this->arrayProxyFactory;
break;
default:
case 'O':
$factory=$this->proxyFactory;
}
return $factory;
}
function link(&$arg,&$newArg) {
$arg->linkResult($newArg->val);
$newArg->parentArg=$arg;
}
function getExact($str) {
return hexdec($str);
}
function getInexact($str) {
$val=null;
sscanf($str,"%e",$val);
return $val;
}
function begin($name,$st) {
$arg=$this->arg;
switch($name[0]) {
case 'A':
$object=$this->globalRef->get($this->getExact($st['v']));
$newArg=new java_ApplyArg($this,'A',
$this->parser->getData($st['m']),
$this->parser->getData($st['p']),
$object,
$this->getExact($st['n']));
$this->link($arg,$newArg);
array_push($this->stack,$this->arg=$newArg);
break;
case 'X':
$newArg=new java_CompositeArg($this,$st['t']);
$this->link($arg,$newArg);
array_push($this->stack,$this->arg=$newArg);
break;
case 'P':
if($arg->type=='H') {
$s=$st['t'];
if($s[0]=='N') {
$arg->setIndex($this->getExact($st['v']));
} else {
$arg->setIndex($this->parser->getData($st['v']));
}
} else {
$arg->setNextIndex();
}
break;
case 'S':
$arg->setResult($this->parser->getData($st['v']));
break;
case 'B':
$s=$st['v'];
$arg->setResult($s[0]=='T');
break;
case 'L':
$sign=$st['p'];
$val=$this->getExact($st['v']);
if($sign[0]=='A') $val*=-1;
$arg->setResult($val);
break;
case 'D':
$arg->setResult($this->getInexact($st['v']));
break;
case 'V':
if ($st['n']!='T') {
$arg->setVoidSignature();
}
case 'N':
$arg->setResult(null);
break;
case 'F':
break;
case 'O':
$arg->setFactory($this->getProxyFactory($st['p']));
$arg->setResult($this->asyncCtx=$this->getExact($st['v']));
if($st['n']!='T') $arg->setSignature($st['m']);
break;
case 'E':
$arg->setFactory($this->throwExceptionProxyFactory);
$arg->setException($st['m']);
$arg->setResult($this->asyncCtx=$this->getExact($st['v']));
break;
default:
$this->parser->parserError();
}
}
function end($name) {
switch($name[0]) {
case 'X':
$frame=array_pop($this->stack);
$this->arg=$frame->parentArg;
break;
}
}
function createParserString() {
return new java_ParserString();
}
function writeArg($arg) {
if(is_string($arg)) {
$this->protocol->writeString($arg);
} else if(is_object($arg)) {
if ((!$arg instanceof java_JavaType)) {
error_log((string)new java_IllegalArgumentException($arg));
trigger_error("argument '".get_class($arg)."' is not a Java object,using NULL instead",E_USER_WARNING);
$this->protocol->writeObject(null);
} else {
$this->protocol->writeObject($arg->__java);
}
} else if(is_null($arg)) {
$this->protocol->writeObject(null);
} else if(is_bool($arg)) {
$this->protocol->writeBoolean($arg);
} else if(is_integer($arg)) {
$this->protocol->writeLong($arg);
} else if(is_float($arg)) {
$this->protocol->writeDouble($arg);
} else if(is_array($arg)) {
$wrote_begin=false;
foreach($arg as $key=>$val) {
if(is_string($key)) {
if(!$wrote_begin) {
$wrote_begin=1;
$this->protocol->writeCompositeBegin_h();
}
$this->protocol->writePairBegin_s($key);
$this->writeArg($val);
$this->protocol->writePairEnd();
} else {
if(!$wrote_begin) {
$wrote_begin=1;
$this->protocol->writeCompositeBegin_h();
}
$this->protocol->writePairBegin_n($key);
$this->writeArg($val);
$this->protocol->writePairEnd();
}
}
if(!$wrote_begin) {
$this->protocol->writeCompositeBegin_a();
}
$this->protocol->writeCompositeEnd();
}
}
function writeArgs($args) {
$this->inArgs=true;
$n=count($args);
for($i=0; $i<$n; $i++) {
$this->writeArg($args[$i]);
}
$this->inArgs=false;
}
function createObject($name,$args) {
$this->protocol->createObjectBegin($name);
$this->writeArgs($args);
$this->protocol->createObjectEnd();
$val=$this->getInternalResult();
return $val;
}
function referenceObject($name,$args) {
$this->protocol->referenceBegin($name);
$this->writeArgs($args);
$this->protocol->referenceEnd();
$val=$this->getInternalResult();
return $val;
}
function getProperty($object,$property) {
$this->protocol->propertyAccessBegin($object,$property);
$this->protocol->propertyAccessEnd();
return $this->getResult();
}
function setProperty($object,$property,$arg) {
$this->protocol->propertyAccessBegin($object,$property);
$this->writeArg($arg);
$this->protocol->propertyAccessEnd();
$this->getResult();
}
function invokeMethod($object,$method,$args) {
$this->protocol->invokeBegin($object,$method);
$this->writeArgs($args);
$this->protocol->invokeEnd();
$val=$this->getResult();
return $val;
}
function unref($object) {
if (isset($this->protocol)) $this->protocol->writeUnref($object);
}
function apply($arg) {
$name=$arg->p;
$object=$arg->v;
$ob=($object==null) ? $name : array(&$object,$name);
$isAsync=$this->isAsync;
$methodCache=$this->methodCache;
$currentArgumentsFormat=$this->currentArgumentsFormat;
try {
$res=$arg->getResult(true);
if((($object==null) && !function_exists($name)) || (!($object==null) && !method_exists($object,$name))) throw new JavaException("java.lang.NoSuchMethodError","$name");
$res=call_user_func_array($ob,$res);
if (is_object($res) && (!($res instanceof java_JavaType))) {
trigger_error("object returned from $name() is not a Java object",E_USER_WARNING);
$this->protocol->invokeBegin(0,"makeClosure");
$this->protocol->writeULong($this->globalRef->add($res));
$this->protocol->invokeEnd();
$res=$this->getResult();
}
$this->protocol->resultBegin();
$this->writeArg($res);
$this->protocol->resultEnd();
} catch (JavaException $e) {
$trace=$e->getTraceAsString();
$this->protocol->resultBegin();
$this->protocol->writeException($e->__java,$trace);
$this->protocol->resultEnd();
} catch(Exception $ex) {
error_log($ex->__toString());
trigger_error("Unchecked exception detected in callback",E_USER_ERROR);
die (1);
}
$this->isAsync=$isAsync;
$this->methodCache=$methodCache;
$this->currentArgumentsFormat=$currentArgumentsFormat;
}
function cast($object,$type) {
switch($type[0]) {
case 'S': case 's':
return $this->invokeMethod(0,"castToString",array($object));
case 'B': case 'b':
return $this->invokeMethod(0,"castToBoolean",array($object));
case 'L': case 'I': case 'l': case 'i':
return $this->invokeMethod(0,"castToExact",array($object));
case 'D': case 'd': case 'F': case 'f':
return $this->invokeMethod(0,"castToInExact",array($object));
case 'N': case 'n':
return null;
case 'A': case 'a':
return $this->invokeMethod(0,"castToArray",array($object));
case 'O': case 'o':
return $object;
default:
throw new java_RuntimeException("$type illegal");
}
}
function getContext() {
static $cache=null;
if (!is_null($cache)) return $cache;
return $cache=$this->invokeMethod(0,"getContext",array());
}
function getSession($args) {
return $this->invokeMethod(0,"getSession",$args);
}
function getServerName() {
static $cache=null;
if (!is_null($cache)) return $cache;
return $cache=$this->protocol->getServerName();
}
}
function java_shutdown() {
global $java_initialized;
if (!$java_initialized) return;
if (session_id()) session_write_close();
$client=__javaproxy_Client_getClient();
if (!isset($client->protocol) || $client->inArgs) return;
if ($client->preparedToSendBuffer)
$client->sendBuffer.=$client->preparedToSendBuffer;
$client->sendBuffer.=$client->protocol->getKeepAlive();
$client->protocol->flush();
$client->protocol->keepAlive();
}
register_shutdown_function("java_shutdown");
class java_GlobalRef {
public $map;
function java_GlobalRef() {
$this->map=array();
}
function add($object) {
if(is_null($object)) return 0;
return array_push($this->map,$object);
}
function get($id) {
if(!$id) return 0;
return $this->map[--$id];
}
}
class java_NativeParser {
public $parser,$handler;
public $level,$event;
public $buf;
function java_NativeParser($handler) {
$this->handler=$handler;
$this->parser=xml_parser_create();
xml_parser_set_option($this->parser,XML_OPTION_CASE_FOLDING,0);
xml_set_object($this->parser,$this);
xml_set_element_handler($this->parser,"begin","end");
xml_parse($this->parser,"<F>");
$this->level=0;
}
function begin($parser,$name,$param) {
$this->event=true;
switch($name) {
case 'X': case 'A': $this->level+=1;
}
$this->handler->begin($name,$param);
}
function end($parser,$name) {
$this->handler->end($name);
switch($name) {
case 'X': case 'A': $this->level-=1;
}
}
function getData($str) {
return base64_decode($str);
}
function parse() {
do {
$this->event=false;
$buf=$this->buf=$this->handler->read(JAVA_RECV_SIZE);
$len=strlen($buf);
if(!xml_parse($this->parser,$buf,$len==0)) {
$this->handler->protocol->handler->shutdownBrokenConnection(
sprintf("protocol error: %s,%s at col %d. Check the back end log for OutOfMemoryErrors.",
$buf,
xml_error_string(xml_get_error_code($this->parser)),
xml_get_current_column_number($this->parser)));
}
} while(!$this->event || $this->level>0);
}
function parserError() {
$this->handler->protocol->handler->shutdownBrokenConnection(
sprintf("protocol error: %s. Check the back end log for details.",$this->buf));
}
}
class java_Parser {
public $parser;
function java_Parser($handler) {
if(function_exists("xml_parser_create")) {
$this->parser=new java_NativeParser($handler);
$handler->RUNTIME["PARSER"]="NATIVE";
} else {
$this->parser=new java_SimpleParser($handler);
$handler->RUNTIME["PARSER"]="SIMPLE";
}
}
function parse() {
$this->parser->parse();
}
function getData($str) {
return $this->parser->getData($str);
}
function parserError() {
$this->parser->parserError();
}
}
function java_getHeader($name,$array) {
if (array_key_exists($name,$array)) return $array[$name];
$name="HTTP_$name";
if (array_key_exists($name,$array)) return $array[$name];
return null;
}
function java_checkCliSapi() {
$sapi=substr(php_sapi_name(),0,3);
return ((($sapi=='cgi') && !get_cfg_var("java.session")) || ($sapi=='cli'));
}
function java_getCompatibilityOption($client) {
static $compatibility=null;
if ($compatibility) return $compatibility;
$compatibility=$client->RUNTIME["PARSER"]=="NATIVE"
? (0103-JAVA_PREFER_VALUES)
: (0100+JAVA_PREFER_VALUES);
if(is_int(JAVA_LOG_LEVEL)) {
$compatibility |=128 | (7 & JAVA_LOG_LEVEL)<<2;
}
$compatibility=chr ($compatibility);
return $compatibility;
}
class java_EmptyChannel {
protected $handler;
private $res;
function java_EmptyChannel($handler) {
$this->handler=$handler;
}
function shutdownBrokenConnection () {}
function fwrite($data) {
return $this->handler->fwrite($data);
}
function fread($size) {
return $this->handler->fread($size);
}
function getKeepAliveA() {
return "<F p=\"A\" />";
}
function getKeepAliveE() {
return "<F p=\"E\" />";
}
function getKeepAlive() {
return $this->getKeepAliveE();
}
function error() {
trigger_error("An unchecked exception occured during script execution. Please check the server log files for details.",E_USER_ERROR);
}
function checkA($peer) {
$val=$this->res[6];
if ($val !='A') fclose($peer);
if ($val !='A' && $val !='E') {
$this->error();
}
}
function checkE() {
$val=$this->res[6];
if ($val !='E') {
$this->error();
}
}
function keepAliveS() {
$this->res=$this->fread(10);
}
function keepAliveSC() {
$this->res=$this->fread(10);
$this->fwrite("");
$this->fread(JAVA_RECV_SIZE);
}
function keepAliveH() {
$this->res=$this->handler->read(10);
}
function keepAlive() {
$this->keepAliveH();
$this->checkE();
}
}
abstract class java_SocketChannel extends java_EmptyChannel {
public $peer,$host;
function java_SocketChannel($peer,$host) {
$this->peer=$peer;
$this->host=$host;
}
function fwrite($data) {
return fwrite($this->peer,$data);
}
function fread($size) {
return fread($this->peer,$size);
}
function shutdownBrokenConnection () {
fclose($this->peer);
}
}
class java_SocketChannelP extends java_SocketChannel {
function getKeepAlive() {return $this->getKeepAliveA();}
function keepAlive() { $this->keepAliveS(); $this->checkA($this->peer); }
}
class java_ChunkedSocketChannel extends java_SocketChannel {
function fwrite($data) {
$len=dechex(strlen($data));
return fwrite($this->peer,"${len}\r\n${data}\r\n");
}
function fread($size) {
$length=hexdec(fgets($this->peer,JAVA_RECV_SIZE));
$data="";
while ($length > 0) {
$str=fread($this->peer,$length);
if (feof ($this->peer)) return null;
$length -=strlen($str);
$data .=$str;
}
fgets($this->peer,3);
return $data;
}
function keepAlive() { $this->keepAliveSC(); $this->checkE(); fclose ($this->peer); }
}
class java_SocketHandler {
public $protocol,$channel;
function java_SocketHandler($protocol,$channel) {
$this->protocol=$protocol;
$this->channel=$channel;
}
function write($data) {
return $this->channel->fwrite($data);
}
function fwrite($data) {return $this->write($data);}
function read($size) {
return $this->channel->fread($size);
}
function fread($size) {return $this->read($size);}
function redirect() {}
function getKeepAlive() {
return $this->channel->getKeepAlive();
}
function keepAlive() {
$this->channel->keepAlive();
}
function dieWithBrokenConnection($msg) {
unset($this->protocol->client->protocol);
trigger_error ($msg?$msg:"unknown error: please see back end log for details",E_USER_ERROR);
}
function shutdownBrokenConnection ($msg) {
$this->channel->shutdownBrokenConnection();
$this->dieWithBrokenConnection($msg);
}
}
class java_SimpleHttpHandler extends java_SocketHandler {
public $headers,$cookies;
public $context,$ssl,$port;
public $host;
function createChannel() {
$channelName=java_getHeader("X_JAVABRIDGE_REDIRECT",$_SERVER);
$context=java_getHeader("X_JAVABRIDGE_CONTEXT",$_SERVER);
$len=strlen($context);
$len0=java_getCompatibilityOption($this->protocol->client);
$len1=chr($len&0xFF); $len>>=8;
$len2=chr($len&0xFF);
$this->channel=new java_EmptyChannel($this);
$this->channel=$this->getChannel($channelName);
$this->protocol->socketHandler=new java_SocketHandler($this->protocol,$this->channel);
$this->protocol->write("\177${len0}${len1}${len2}${context}");
$this->context=sprintf("X_JAVABRIDGE_CONTEXT: %s\r\n",$context);
$this->protocol->handler=$this->protocol->socketHandler;
$this->protocol->handler->write($this->protocol->client->sendBuffer)
or $this->protocol->handler->shutdownBrokenConnection("Broken local connection handle");
$this->protocol->client->sendBuffer=null;
$this->protocol->handler->read(1)
or $this->protocol->handler->shutdownBrokenConnection("Broken local connection handle");
}
function java_SimpleHttpHandler($protocol,$ssl,$host,$port) {
$this->cookies=array();
$this->protocol=$protocol;
$this->ssl=$ssl;
$this->host=$host;
$this->port=$port;
$this->createChannel();
}
function getCookies() {
$str="";
$first=true;
foreach($_COOKIE as $k=> $v) {
$str .=($first ? "Cookie: $k=$v":"; $k=$v");
$first=false;
}
if(!$first) $str .="\r\n";
return $str;
}
function getContextFromCgiEnvironment() {
$ctx=java_getHeader('X_JAVABRIDGE_CONTEXT',$_SERVER);
return $ctx;
}
function getContext() {
static $context=null;
if($context) return $context;
$ctx=$this->getContextFromCgiEnvironment();
$context="";
if($ctx) {
$context=sprintf("X_JAVABRIDGE_CONTEXT: %s\r\n",$ctx);
}
return $context;
}
function getWebAppInternal() {
$context=$this->protocol->webContext;
if(isset($context)) return $context;
return (JAVA_SERVLET=="User" &&
array_key_exists('PHP_SELF',$_SERVER) &&
array_key_exists('HTTP_HOST',$_SERVER))
? $_SERVER['PHP_SELF']."javabridge"
: null;
}
function getWebApp() {
$context=$this->getWebAppInternal();
if(is_null($context)) $context=JAVA_SERVLET;
if(is_null($context) || $context[0]!="/")
$context="/JavaBridge/JavaBridge.phpjavabridge";
return $context;
}
function write($data) {
return $this->protocol->socketHandler->write($data);
}
function doSetCookie($key,$val,$path) {
$path=trim($path);
$webapp=$this->getWebAppInternal(); if(!$webapp) $path="/";
setcookie($key,$val,0,$path);
}
function read($size) {
return $this->protocol->socketHandler->read($size);
}
function getChannel($channelName) {
$errstr=null; $errno=null;
$peer=pfsockopen($this->host,$channelName,$errno,$errstr,20);
if (!$peer) throw new java_IllegalStateException("No ContextServer for {$this->host}:{$channelName}. Error: $errstr ($errno)\n");
stream_set_timeout($peer,-1);
return new java_SocketChannelP($peer,$this->host);
}
function keepAlive() {
parent::keepAlive();
}
function redirect() {}
}
class java_SimpleHttpTunnelHandler extends java_SimpleHttpHandler {
public $socket;
protected $hasContentLength=false;
function createSimpleChannel () {
$this->channel=new java_EmptyChannel($this);
}
function createChannel() {
$this->createSimpleChannel();
}
function shutdownBrokenConnection ($msg) {
fclose($this->socket);
$this->dieWithBrokenConnection($msg);
}
function checkSocket($socket,&$errno,&$errstr) {
if (!$socket) {
$msg="Could not connect to the JEE server {$this->ssl}{$this->host}:{$this->port}. Please start it.";
$msg.=java_checkCliSapi()
?" Or define('JAVA_HOSTS',9267); define('JAVA_SERVLET',false); before including 'Java.inc' and try again. Error message: $errstr ($errno)\n"
:" Error message: $errstr ($errno)\n";
throw new java_ConnectException($msg);
}
}
function open() {
$errno=null; $errstr=null;
$socket=fsockopen("{$this->ssl}{$this->host}",$this->port,$errno,$errstr,20);
$this->checkSocket($socket,$errno,$errstr);
stream_set_timeout($socket,-1);
$this->socket=$socket;
}
function fread($size) {
$length=hexdec(fgets($this->socket,JAVA_RECV_SIZE));
$data="";
while ($length > 0) {
$str=fread($this->socket,$length);
if (feof ($this->socket)) return null;
$length -=strlen($str);
$data .=$str;
}
fgets($this->socket,3);
return $data;
}
function fwrite($data) {
$len=dechex(strlen($data));
return fwrite($this->socket,"${len}\r\n${data}\r\n");
}
function close() {
fwrite($this->socket,"0\r\n\r\n");
fgets($this->socket,JAVA_RECV_SIZE);
fgets($this->socket,3);
fclose($this->socket);
}
function java_SimpleHttpTunnelHandler($protocol,$ssl,$host,$port) {
parent::java_SimpleHttpHandler($protocol,$ssl,$host,$port);
$this->open();
}
function read($size) {
if(is_null($this->headers)) $this->parseHeaders();
if (isset($this->headers["http_error"])) {
if (isset($this->headers["transfer_chunked"])) {
$str=$this->fread(JAVA_RECV_SIZE);
} elseif (isset($this->headers['content_length'])) {
$len=$this->headers['content_length'];
for($str=fread($this->socket,$len); strlen($str)<$len; $str.=fread($this->socket,$len-strlen($str)))
if (feof ($this->socket)) break;
} else {
$str=fread($this->socket,JAVA_RECV_SIZE);
}
$this->shutdownBrokenConnection($str);
}
return $this->fread(JAVA_RECV_SIZE);
}
function getBodyFor ($compat,$data) {
$len=dechex(2+strlen($data));
return "Cache-Control: no-cache\r\nPragma: no-cache\r\nTransfer-Encoding: chunked\r\n\r\n${len}\r\n\177${compat}${data}\r\n";
}
function write($data) {
$compat=java_getCompatibilityOption($this->protocol->client);
$this->headers=null;
$socket=$this->socket;
$webapp=$this->getWebApp();
$cookies=$this->getCookies();
$context=$this->getContext();
$res="PUT ";
$res .=$webapp;
$res .=" HTTP/1.1\r\n";
$res .="Host: {$this->host}:{$this->port}\r\n";
$res .=$context;
$res .=$cookies;
$res .=$this->getBodyFor($compat,$data);
$count=fwrite($socket,$res) or $this->shutdownBrokenConnection("Broken connection handle");
fflush($socket) or $this->shutdownBrokenConnection("Broken connection handle");
return $count;
}
function parseHeaders() {
$this->headers=array();
$line=trim(fgets($this->socket,JAVA_RECV_SIZE));
$ar=explode (" ",$line);
$code=((int)$ar[1]);
if ($code !=200) $this->headers["http_error"]=$code;
while (($str=trim(fgets($this->socket,JAVA_RECV_SIZE)))) {
if($str[0]=='X') {
if(!strncasecmp("X_JAVABRIDGE_REDIRECT",$str,21)) {
$this->headers["redirect"]=trim(substr($str,22));
} else if(!strncasecmp("X_JAVABRIDGE_CONTEXT",$str,20)) {
$this->headers["context"]=trim(substr($str,21));
}
} else if($str[0]=='S') {
if(!strncasecmp("SET-COOKIE",$str,10)) {
$str=substr($str,12);
$this->cookies[]=$str;
$ar=explode(";",$str);
$cookie=explode("=",$ar[0]);
$path="";
if(isset($ar[1])) $p=explode("=",$ar[1]);
if(isset($p)) $path=$p[1];
$this->doSetCookie($cookie[0],$cookie[1],$path);
}
} else if($str[0]=='C') {
if(!strncasecmp("CONTENT-LENGTH",$str,14)) {
$this->headers["content_length"]=trim(substr($str,15));
$this->hasContentLength=true;
} else if(!strncasecmp("CONNECTION",$str,10) && !strncasecmp("close",trim(substr($str,11)),5)) {
$this->headers["connection_close"]=true;
}
} else if($str[0]=='T') {
if(!strncasecmp("TRANSFER-ENCODING",$str,17) && !strncasecmp("chunked",trim(substr($str,18)),7)) {
$this->headers["transfer_chunked"]=true;
}
}
}
}
function getSimpleChannel() {
return new java_ChunkedSocketChannel($this->socket,$this->protocol,$this->host);
}
function redirect() {
$this->isRedirect=isset($this->headers["redirect"]);
if ($this->isRedirect)
$channelName=$this->headers["redirect"];
$context=$this->headers["context"];
$len=strlen($context);
$len0=chr(0xFF);
$len1=chr($len&0xFF); $len>>=8;
$len2=chr($len&0xFF);
if ($this->isRedirect) {
$this->protocol->socketHandler=new java_SocketHandler($this->protocol,$this->getChannel($channelName));
$this->protocol->write("\177${len0}${len1}${len2}${context}");
$this->context=sprintf("X_JAVABRIDGE_CONTEXT: %s\r\n",$context);
$this->close ();
$this->protocol->handler=$this->protocol->socketHandler;
$this->protocol->handler->write($this->protocol->client->sendBuffer)
or $this->protocol->handler->shutdownBrokenConnection("Broken local connection handle");
$this->protocol->client->sendBuffer=null;
$this->protocol->handler->read(1)
or $this->protocol->handler->shutdownBrokenConnection("Broken local connection handle");
} else {
$this->protocol->handler=$this->protocol->socketHandler=new java_SocketHandler($this->protocol,$this->getSimpleChannel());
}
}
}
class java_HttpTunnelHandler extends java_SimpleHttpTunnelHandler {
function fread($size) {
if ($this->hasContentLength)
return fread($this->socket,$this->headers["content_length"]);
else
return parent::fread($size);
}
function fwrite($data) {
if ($this->hasContentLength)
return fwrite($this->socket,$data);
else
return parent::fwrite($data);
}
function close() {
if ($this->hasContentLength) {
fwrite($this->socket,"0\r\n\r\n");
fclose($this->socket);
} else {
parent::fclose($this->socket);
}
}
}
class java_Protocol {
public $client;
public $webContext;
public $serverName;
function getOverrideHosts() {
if(array_key_exists('X_JAVABRIDGE_OVERRIDE_HOSTS',$_ENV)) {
$override=$_ENV['X_JAVABRIDGE_OVERRIDE_HOSTS'];
if(!is_null($override) && $override!='/') return $override;
}
return
java_getHeader('X_JAVABRIDGE_OVERRIDE_HOSTS_REDIRECT',$_SERVER);
}
static function getHost() {
static $host=null;
if(is_null($host)) {
$hosts=explode(";",JAVA_HOSTS);
$host=explode(":",$hosts[0]);
while(count ($host) < 3) array_unshift($host,"");
if (substr($host[1],0,2)=="//") $host[1]=substr($host[1],2);
}
return $host;
}
function createHttpHandler() {
$overrideHosts=$this->getOverrideHosts();
$ssl="";
if($overrideHosts) {
$s=$overrideHosts;
if((strlen($s)>2) && ($s[1]==':')) {
if($s[0]=='s')
$ssl="ssl://";
$s=substr($s,2);
}
$webCtx=strpos($s,"//");
if($webCtx)
$host=substr($s,0,$webCtx);
else
$host=$s;
$idx=strpos($host,':');
if($idx) {
if($webCtx)
$port=substr($host,$idx+1,$webCtx);
else
$port=substr($host,$idx+1);
$host=substr($host,0,$idx);
} else {
$port="8080";
}
if($webCtx) $webCtx=substr($s,$webCtx+1);
$this->webContext=$webCtx;
} else {
$hostVec=java_Protocol::getHost();
if ($ssl=$hostVec[0]) $ssl .="://";
$host=$hostVec[1];
$port=$hostVec[2];
}
$this->serverName="${ssl}${host}:$port";
if ((array_key_exists("X_JAVABRIDGE_REDIRECT",$_SERVER)) ||
(array_key_exists("HTTP_X_JAVABRIDGE_REDIRECT",$_SERVER)))
return new java_SimpleHttpHandler($this,$ssl,$host,$port);
return new java_HttpTunnelHandler($this,$ssl,$host,$port);
}
function createSimpleHandler($name,$again=true) {
$channelName=$name;
$errno=null; $errstr=null;
if(is_numeric($channelName)) {
$peer=@pfsockopen($host="127.0.0.1",$channelName,$errno,$errstr,5);
} else {
$type=$channelName[0];
list($host,$channelName)=explode(":",$channelName);
$peer=pfsockopen($host,$channelName,$errno,$errstr,20);
if (!$peer)
throw new java_ConnectException("No Java server at $host:$channelName. Error message: $errstr ($errno)");
}
if (!$peer) {
$java=file_exists(ini_get("extension_dir")."/JavaBridge.jar")?ini_get("extension_dir")."/JavaBridge.jar":(java_get_base()."/JavaBridge.jar");
if (!file_exists($java))
throw new java_IOException("Could not find $java in ".getcwd().". Download it from http://sf.net/projects/php-java-bridge/files/Binary%20package/php-java-bridge_".JAVA_PEAR_VERSION."/exploded/JavaBridge.jar/download and try again.");
$java_cmd="java -Dphp.java.bridge.daemon=true -jar \"${java}\" INET_LOCAL:$channelName 0";
if (!$again)
throw new java_ConnectException("No Java back end! Please run it with: $java_cmd. Error message: $errstr ($errno)");
if (!java_checkCliSapi())
trigger_error("This PHP SAPI requires a JEE or SERVLET back end. Start it,define ('JAVA_SERVLET',true); define('JAVA_HOSTS',...); and try again.",E_USER_ERROR);
system ($java_cmd);
return $this->createSimpleHandler($name,false);
}
stream_set_timeout($peer,-1);
$handler=new java_SocketHandler($this,new java_SocketChannelP($peer,$host));
$compatibility=java_getCompatibilityOption($this->client);
$this->write("\177$compatibility");
$this->serverName="127.0.0.1:$channelName";
return $handler;
}
function java_get_simple_channel() {
return (JAVA_HOSTS&&(!JAVA_SERVLET||(JAVA_SERVLET=="Off"))) ? JAVA_HOSTS : null;
}
function createHandler() {
if(!java_getHeader('X_JAVABRIDGE_OVERRIDE_HOSTS',$_SERVER)&&
((function_exists("java_get_default_channel")&&($defaultChannel=java_get_default_channel())) ||
($defaultChannel=$this->java_get_simple_channel())) ) {
return $this->createSimpleHandler($defaultChannel);
} else {
return $this->createHttpHandler();
}
}
function java_Protocol ($client) {
$this->client=$client;
$this->handler=$this->createHandler();
}
function redirect() {
$this->handler->redirect();
}
function read($size) {
return $this->handler->read($size);
}
function sendData() {
$this->handler->write($this->client->sendBuffer);
$this->client->sendBuffer=null;
}
function flush() {
$this->sendData();
}
function getKeepAlive() {
return $this->handler->getKeepAlive();
}
function keepAlive() {
$this->handler->keepAlive();
}
function handle() {
$this->client->handleRequests();
}
function write($data) {
$this->client->sendBuffer.=$data;
}
function finish() {
$this->flush();
$this->handle();
$this->redirect();
}
function referenceBegin($name) {
$this->client->sendBuffer.=$this->client->preparedToSendBuffer;
$this->client->preparedToSendBuffer=null;
$signature=sprintf("<H p=\"1\" v=\"%s\">",$name);
$this->write($signature);
$signature[6]="2";
$this->client->currentArgumentsFormat=$signature;
}
function referenceEnd() {
$this->client->currentArgumentsFormat.=$format="</H>";
$this->write($format);
$this->finish();
$this->client->currentCacheKey=null;
}
function createObjectBegin($name) {
$this->client->sendBuffer.=$this->client->preparedToSendBuffer;
$this->client->preparedToSendBuffer=null;
$signature=sprintf("<K p=\"1\" v=\"%s\">",$name);
$this->write($signature);
$signature[6]="2";
$this->client->currentArgumentsFormat=$signature;
}
function createObjectEnd() {
$this->client->currentArgumentsFormat.=$format="</K>";
$this->write($format);
$this->finish();
$this->client->currentCacheKey=null;
}
function propertyAccessBegin($object,$method) {
$this->client->sendBuffer.=$this->client->preparedToSendBuffer;
$this->client->preparedToSendBuffer=null;
$this->write(sprintf("<G p=\"1\" v=\"%x\" m=\"%s\">",$object,$method));
$this->client->currentArgumentsFormat="<G p=\"2\" v=\"%x\" m=\"${method}\">";
}
function propertyAccessEnd() {
$this->client->currentArgumentsFormat.=$format="</G>";
$this->write($format);
$this->finish();
$this->client->currentCacheKey=null;
}
function invokeBegin($object,$method) {
$this->client->sendBuffer.=$this->client->preparedToSendBuffer;
$this->client->preparedToSendBuffer=null;
$this->write(sprintf("<Y p=\"1\" v=\"%x\" m=\"%s\">",$object,$method));
$this->client->currentArgumentsFormat="<Y p=\"2\" v=\"%x\" m=\"${method}\">";
}
function invokeEnd() {
$this->client->currentArgumentsFormat.=$format="</Y>";
$this->write($format);
$this->finish();
$this->client->currentCacheKey=null;
}
function resultBegin() {
$this->client->sendBuffer.=$this->client->preparedToSendBuffer;
$this->client->preparedToSendBuffer=null;
$this->write("<R>");
}
function resultEnd() {
$this->client->currentCacheKey=null;
$this->write("</R>");
$this->flush();
}
function writeString($name) {
$this->client->currentArgumentsFormat.=$format="<S v=\"%s\"/>";
$this->write(sprintf($format,htmlspecialchars($name,ENT_COMPAT)));
}
function writeBoolean($boolean) {
$this->client->currentArgumentsFormat.=$format="<T v=\"%s\"/>";
$this->write(sprintf($format,$boolean));
}
function writeLong($l) {
$this->client->currentArgumentsFormat.="<J v=\"%d\"/>";
if($l<0) {
$this->write(sprintf("<L v=\"%x\" p=\"A\"/>",-$l));
} else {
$this->write(sprintf("<L v=\"%x\" p=\"O\"/>",$l));
}
}
function writeULong($l) {
$this->client->currentArgumentsFormat.=$format="<L v=\"%x\" p=\"O\"/>";
$this->write(sprintf($format,$l));
}
function writeDouble($d) {
$this->client->currentArgumentsFormat.=$format="<D v=\"%.14e\"/>";
$this->write(sprintf($format,$d));
}
function writeObject($object) {
$this->client->currentArgumentsFormat.=$format="<O v=\"%x\"/>";
$this->write(sprintf($format,$object));
}
function writeException($object,$str) {
$this->write(sprintf("<E v=\"%x\" m=\"%s\"/>",$object,htmlspecialchars($str,ENT_COMPAT)));
}
function writeCompositeBegin_a() {
$this->write("<X t=\"A\">");
}
function writeCompositeBegin_h() {
$this->write("<X t=\"H\">");
}
function writeCompositeEnd() {
$this->write("</X>");
}
function writePairBegin_s($key) {
$this->write(sprintf("<P t=\"S\" v=\"%s\">",htmlspecialchars($key,ENT_COMPAT)));
}
function writePairBegin_n($key) {
$this->write(sprintf("<P t=\"N\" v=\"%x\">",$key));
}
function writePairBegin() {
$this->write("<P>");
}
function writePairEnd() {
$this->write("</P>");
}
function writeUnref($object) {
$this->client->sendBuffer.=$this->client->preparedToSendBuffer;
$this->client->preparedToSendBuffer=null;
$this->write(sprintf("<U v=\"%x\"/>",$object));
}
function getServerName() {
return $this->serverName;
}
}
class java_ParserString {
public $string,$off,$length;
function toString() {
return $this->getString();
}
function getString() {
return substr($this->string,$this->off,$this->length);
}
}
class java_ParserTag {
public $n,$strings;
function java_ParserTag() {
$this->strings=array();
$this->n=0;
}
}
class java_SimpleParser {
public $SLEN=256;
public $handler;
public $tag,$buf,$len,$s;
public $type;
function java_SimpleParser($handler) {
$this->handler=$handler;
$this->tag=array(new java_ParserTag(),new java_ParserTag(),new java_ParserTag());
$this->len=$this->SLEN;
$this->s=str_repeat(" ",$this->SLEN);
$this->type=$this->VOJD;
}
public $BEGIN=0,$KEY=1,$VAL=2,$ENTITY=3,$VOJD=5,$END=6;
public $level=0,$eor=0; public $in_dquote,$eot=false;
public $pos=0,$c=0,$i=0,$i0=0,$e;
function RESET() {
$this->type=$this->VOJD;
$this->level=0;
$this->eor=0;
$this->in_dquote=false;
$this->i=0;
$this->i0=0;
}
function APPEND($c) {
if($this->i>=$this->len-1) {
$this->s=str_repeat($this->s,2);
$this->len*=2;
}
$this->s[$this->i++]=$c;
}
function CALL_BEGIN() {
$pt=&$this->tag[1]->strings;
$st=&$this->tag[2]->strings;
$t=&$this->tag[0]->strings[0];
$name=$t->string[$t->off];
$n=$this->tag[2]->n;
$ar=array();
for($i=0; $i<$n; $i++) {
$ar[$pt[$i]->getString()]=$st[$i]->getString();
}
$this->handler->begin($name,$ar);
}
function CALL_END() {
$t=&$this->tag[0]->strings[0];
$name=$t->string[$t->off];
$this->handler->end($name);
}
function PUSH($t) {
$str=&$this->tag[$t]->strings;
$n=&$this->tag[$t]->n;
$this->s[$this->i]='|';
if(!isset($str[$n])){$h=$this->handler; $str[$n]=$h->createParserString();}
$str[$n]->string=&$this->s;
$str[$n]->off=$this->i0;
$str[$n]->length=$this->i-$this->i0;
++$this->tag[$t]->n;
$this->APPEND('|');
$this->i0=$this->i;
}
function parse() {
while($this->eor==0) {
if($this->c>=$this->pos) {
$this->buf=$this->handler->read(JAVA_RECV_SIZE);
if(is_null($this->buf) || strlen($this->buf)==0)
$this->handler->protocol->handler->shutdownBrokenConnection("protocol error. Check the back end log for OutOfMemoryErrors.");
$this->pos=strlen($this->buf);
if($this->pos==0) break;
$this->c=0;
}
switch(($ch=$this->buf[$this->c]))
{
case '<': if($this->in_dquote) {$this->APPEND($ch); break;}
$this->level+=1;
$this->type=$this->BEGIN;
break;
case '\t': case '\f': case '\n': case '\r': case ' ': if($this->in_dquote) {$this->APPEND($ch); break;}
if($this->type==$this->BEGIN) {
$this->PUSH($this->type);
$this->type=$this->KEY;
}
break;
case '=': if($this->in_dquote) {$this->APPEND($ch); break;}
$this->PUSH($this->type);
$this->type=$this->VAL;
break;
case '/': if($this->in_dquote) {$this->APPEND($ch); break;}
if($this->type==$this->BEGIN) { $this->type=$this->END; $this->level-=1; }
$this->level-=1;
$this->eot=true;
break;
case '>': if($this->in_dquote) {$this->APPEND($ch); break;}
if($this->type==$this->END){
$this->PUSH($this->BEGIN);
$this->CALL_END();
} else {
if($this->type==$this->VAL) $this->PUSH($this->type);
$this->CALL_BEGIN();
}
$this->tag[0]->n=$this->tag[1]->n=$this->tag[2]->n=0; $this->i0=$this->i=0;
$this->type=$this->VOJD;
if($this->level==0) $this->eor=1;
break;
case ';':
if($this->type==$this->ENTITY) {
switch ($this->s[$this->e+1]) {
case 'l': $this->s[$this->e]='<'; $this->i=$this->e+1; break;
case 'g': $this->s[$this->e]='>'; $this->i=$this->e+1; break;
case 'a': $this->s[$this->e]=($this->s[$this->e+2]=='m'?'&':'\''); $this->i=$this->e+1; break;
case 'q': $this->s[$this->e]='"'; $this->i=$this->e+1; break;
default: $this->APPEND($ch);
}
$this->type=$this->VAL;
} else {
$this->APPEND($ch);
}
break;
case '&':
$this->type=$this->ENTITY;
$this->e=$this->i;
$this->APPEND($ch);
break;
case '"':
$this->in_dquote=!$this->in_dquote;
if(!$this->in_dquote && $this->type==$this->VAL) {
$this->PUSH($this->type);
$this->type=$this->KEY;
}
break;
default:
$this->APPEND($ch);
}
$this->c+=1;
}
$this->RESET();
}
function getData($str) {
return $str;
}
function parserError() {
$this->handler->protocol->handler->shutdownBrokenConnection(
sprintf("protocol error: %s. Check the back end log for details.",$this->s));
}
}
interface java_JavaType {};
$java_initialized=false;
function __javaproxy_Client_getClient() {
static $client=null;
if(!is_null($client)) return $client;
if (function_exists("java_create_client")) $client=java_create_client();
else {
global $java_initialized;
$client=new java_Client();
$java_initialized=true;
}
return $client;
}
function java_last_exception_get() {
$client=__javaproxy_Client_getClient();
return $client->invokeMethod(0,"getLastException",array());
}
function java_last_exception_clear() {
$client=__javaproxy_Client_getClient();
$client->invokeMethod(0,"clearLastException",array());
}
function java_values_internal($object) {
if(!$object instanceof java_JavaType) return $object;
$client=__javaproxy_Client_getClient();
return $client->invokeMethod(0,"getValues",array($object));
}
function java_invoke($object,$method,$args) {
$client=__javaproxy_Client_getClient();
$id=($object==null) ? 0 : $object->__java;
return $client->invokeMethod($id,$method,$args);
}
function java_unwrap ($object) {
if(!$object instanceof java_JavaType) throw new java_IllegalArgumentException($object);
$client=__javaproxy_Client_getClient();
return $client->globalRef->get($client->invokeMethod(0,"unwrapClosure",array($object)));
}
function java_values($object) {
return java_values_internal($object);
}
function java_inspect_internal($object) {
if(!$object instanceof java_JavaType) throw new java_IllegalArgumentException($object);
$client=__javaproxy_Client_getClient();
return $client->invokeMethod(0,"inspect",array($object));
}
function java_inspect($object) {
return java_inspect_internal($object);
}
function java_set_file_encoding($enc) {
$client=__javaproxy_Client_getClient();
return $client->invokeMethod(0,"setFileEncoding",array($enc));
}
function java_instanceof_internal($ob,$clazz) {
if(!$ob instanceof java_JavaType) throw new java_IllegalArgumentException($ob);
if(!$clazz instanceof java_JavaType) throw new java_IllegalArgumentException($clazz);
$client=__javaproxy_Client_getClient();
return $client->invokeMethod(0,"instanceOf",array($ob,$clazz));
}
function java_instanceof($ob,$clazz) {
return java_instanceof_internal($ob,$clazz);
}
function java_cast_internal($object,$type) {
if(!$object instanceof java_JavaType) {
switch($type[0]) {
case 'S': case 's':
return (string)$object;
case 'B': case 'b':
return (boolean)$object;
case 'L': case 'I': case 'l': case 'i':
return (integer)$object;
case 'D': case 'd': case 'F': case 'f':
return (float) $object;
case 'N': case 'n':
return null;
case 'A': case 'a':
return (array)$object;
case 'O': case 'o':
return (object)$object;
}
}
return $object->__cast($type);
}
function java_cast($object,$type) {
return java_cast_internal($object,$type);
}
function java_require($arg) {
trigger_error('java_require() not supported anymore. Please use <a href="http://php-java-bridge.sourceforge.net/pjb/webapp.php>tomcat or jee hot deployment</a> instead',E_USER_WARNING);
}
function java_get_lifetime ()
{
$session_max_lifetime=ini_get("session.gc_maxlifetime");
return $session_max_lifetime ? (int)$session_max_lifetime : 1440;
}
function java_session_array($args) {
$client=__javaproxy_Client_getClient();
if(!isset($args[0])) $args[0]=null;
if(!isset($args[1]))
$args[1]=0;
elseif ($args[1]===true)
$args[1]=1;
else
$args[1]=2;
if(!isset($args[2])) {
$args[2]=java_get_lifetime ();
}
return $client->getSession($args);
}
function java_session() {
return java_session_array(func_get_args());
}
function java_server_name() {
try {
$client=__javaproxy_Client_getClient();
return $client->getServerName();
} catch (java_ConnectException $ex) {
return null;
}
}
function java_context() {
$client=__javaproxy_Client_getClient();
return $client->getContext();
}
function java_closure_array($args) {
if(isset($args[2]) && ((!($args[2] instanceof java_JavaType))&&!is_array($args[2])))
throw new java_IllegalArgumentException($args[2]);
$client=__javaproxy_Client_getClient();
$args[0]=isset($args[0]) ? $client->globalRef->add($args[0]) : 0;
$client->protocol->invokeBegin(0,"makeClosure");
$n=count($args);
$client->protocol->writeULong($args[0]);
for($i=1; $i<$n; $i++) {
$client->writeArg($args[$i]);
}
$client->protocol->invokeEnd();
$val=$client->getResult();
return $val;
}
function java_closure() {
return java_closure_array(func_get_args());
}
function java_begin_document() {
}
function java_end_document() {
}
class java_JavaProxy implements java_JavaType {
public $__serialID,$__java;
public $__signature;
public $__client;
public $__tempGlobalRef;
function java_JavaProxy($java,$signature){
$this->__java=$java;
$this->__signature=$signature;
$this->__client=__javaproxy_Client_getClient();
}
function __cast($type) {
return $this->__client->cast($this,$type);
}
function __sleep() {
$args=array($this,java_get_lifetime());
$this->__serialID=$this->__client->invokeMethod(0,"serialize",$args);
$this->__tempGlobalRef=$this->__client->globalRef;
return array("__serialID","__tempGlobalRef");
}
function __wakeup() {
$args=array($this->__serialID,java_get_lifetime());
$this->__client=__javaproxy_Client_getClient();
if($this->__tempGlobalRef)
$this->__client->globalRef=$this->__tempGlobalRef;
$this->__tempGlobalRef=null;
$this->__java=$this->__client->invokeMethod(0,"deserialize",$args);
}
function __destruct() {
if(isset($this->__client))
$this->__client->unref($this->__java);
}
function __get($key) {
return $this->__client->getProperty($this->__java,$key);
}
function __set($key,$val) {
$this->__client->setProperty($this->__java,$key,$val);
}
function __call($method,$args) {
return $this->__client->invokeMethod($this->__java,$method,$args);
}
function __toString() {
try {
return $this->__client->invokeMethod(0,"ObjectToString",array($this));
} catch (JavaException $ex) {
trigger_error("Exception in Java::__toString(): ". java_truncate((string)$ex),E_USER_WARNING);
return "";
}
}
}
class java_objectIterator implements Iterator {
private $var;
function java_ObjectIterator($javaProxy) {
$this->var=java_cast ($javaProxy,"A");
}
function rewind() {
reset($this->var);
}
function valid() {
return $this->current() !==false;
}
function next() {
return next($this->var);
}
function key() {
return key($this->var);
}
function current() {
return current($this->var);
}
}
class java_IteratorProxy extends java_JavaProxy implements IteratorAggregate {
function getIterator() {
return new java_ObjectIterator($this);
}
}
class java_ArrayProxy extends java_IteratorProxy implements ArrayAccess {
function offsetExists($idx) {
$ar=array($this,$idx);
return $this->__client->invokeMethod(0,"offsetExists",$ar);
}
function offsetGet($idx) {
$ar=array($this,$idx);
return $this->__client->invokeMethod(0,"offsetGet",$ar);
}
function offsetSet($idx,$val) {
$ar=array($this,$idx,$val);
return $this->__client->invokeMethod(0,"offsetSet",$ar);
}
function offsetUnset($idx) {
$ar=array($this,$idx);
return $this->__client->invokeMethod(0,"offsetUnset",$ar);
}
}
class java_ExceptionProxy extends java_JavaProxy {
function __toExceptionString($trace) {
$args=array($this,$trace);
return $this->__client->invokeMethod(0,"ObjectToString",$args);
}
}
abstract class java_AbstractJava implements IteratorAggregate,ArrayAccess,java_JavaType {
public $__client;
public $__delegate;
public $__serialID;
public $__factory;
public $__java,$__signature;
public $__cancelProxyCreationTag;
function __createDelegate() {
$proxy=$this->__delegate=
$this->__factory->create($this->__java,$this->__signature);
$this->__java=$proxy->__java;
$this->__signature=$proxy->__signature;
}
function __cast($type) {
if(!isset($this->__delegate)) $this->__createDelegate();
return $this->__delegate->__cast($type);
}
function __sleep() {
if(!isset($this->__delegate)) $this->__createDelegate();
$this->__delegate->__sleep();
return array("__delegate");
}
function __wakeup() {
if(!isset($this->__delegate)) $this->__createDelegate();
$this->__delegate->__wakeup();
$this->__java=$this->__delegate->__java;
$this->__client=$this->__delegate->__client;
}
function __get($key) {
if(!isset($this->__delegate)) $this->__createDelegate();
return $this->__delegate->__get($key);
}
function __set($key,$val) {
if(!isset($this->__delegate)) $this->__createDelegate();
$this->__delegate->__set($key,$val);
}
function __call($method,$args) {
if(!isset($this->__delegate)) $this->__createDelegate();
return $this->__delegate->__call($method,$args);
}
function __toString() {
if(!isset($this->__delegate)) $this->__createDelegate();
return $this->__delegate->__toString();
}
function getIterator() {
if(!isset($this->__delegate)) $this->__createDelegate();
if(func_num_args()==0) return $this->__delegate->getIterator();
$args=func_get_args(); return $this->__call("getIterator",$args);
}
function offsetExists($idx) {
if(!isset($this->__delegate)) $this->__createDelegate();
if(func_num_args()==1) return $this->__delegate->offsetExists($idx);
$args=func_get_args(); return $this->__call("offsetExists",$args);
}
function offsetGet($idx) {
if(!isset($this->__delegate)) $this->__createDelegate();
if(func_num_args()==1) return $this->__delegate->offsetGet($idx);
$args=func_get_args(); return $this->__call("offsetGet",$args);
}
function offsetSet($idx,$val) {
if(!isset($this->__delegate)) $this->__createDelegate();
if(func_num_args()==2) return $this->__delegate->offsetSet($idx,$val);
$args=func_get_args(); return $this->__call("offsetSet",$args);
}
function offsetUnset($idx) {
if(!isset($this->__delegate)) $this->__createDelegate();
if(func_num_args()==1) return $this->__delegate->offsetUnset($idx);
$args=func_get_args(); return $this->__call("offsetUnset",$args);
}
}
class Java extends java_AbstractJava {
function Java() {
$client=$this->__client=__javaproxy_Client_getClient();
$args=func_get_args();
$name=array_shift($args);
if(is_array($name)) {$args=$name; $name=array_shift($args);}
$sig="&{$this->__signature}@{$name}";
$len=count($args);
$args2=array();
for($i=0; $i<$len; $i++) {
switch(gettype($val=$args[$i])) {
case 'boolean': array_push($args2,$val); $sig.='@b'; break;
case 'integer': array_push($args2,$val); $sig.='@i'; break;
case 'double': array_push($args2,$val); $sig.='@d'; break;
case 'string': array_push($args2,htmlspecialchars($val,ENT_COMPAT)); $sig.='@s'; break;
case 'array':$sig="~INVALID"; break;
case 'object':
if($val instanceof java_JavaType) {
array_push($args2,$val->__java);
$sig.="@o{$val->__signature}";
}
else {
$sig="~INVALID";
}
break;
case 'resource': array_push($args2,$val); $sig.='@r'; break;
case 'NULL': array_push($args2,$val); $sig.='@N'; break;
case 'unknown type': array_push($args2,$val); $sig.='@u'; break;
default: throw new java_IllegalArgumentException($val);
}
}
if(array_key_exists($sig,$client->methodCache)) {
$cacheEntry=&$client->methodCache[$sig];
$client->sendBuffer.=$client->preparedToSendBuffer;
if(strlen($client->sendBuffer)>=JAVA_SEND_SIZE) {
if($client->protocol->handler->write($client->sendBuffer)<=0)
throw new java_IllegalStateException("Connection out of sync,check backend log for details.");
$client->sendBuffer=null;
}
$client->preparedToSendBuffer=vsprintf($cacheEntry->fmt,$args2);
$this->__java=++$client->asyncCtx;
$this->__factory=$cacheEntry->factory;
$this->__signature=$cacheEntry->signature;
$this->__cancelProxyCreationTag=++$client->cancelProxyCreationTag;
} else {
$client->currentCacheKey=$sig;
$delegate=$this->__delegate=$client->createObject($name,$args);
$this->__java=$delegate->__java;
$this->__signature=$delegate->__signature;
}
}
function __destruct() {
if(!isset($this->__client)) return;
$client=$this->__client;
$preparedToSendBuffer=&$client->preparedToSendBuffer;
if($preparedToSendBuffer &&
$client->cancelProxyCreationTag==$this->__cancelProxyCreationTag) {
$preparedToSendBuffer[6]="3";
$client->sendBuffer.=$preparedToSendBuffer;
$preparedToSendBuffer=null;
$client->asyncCtx -=1;
} else {
if(!isset($this->__delegate)) {
$client->unref($this->__java);
}
}
}
function __call($method,$args) {
$client=$this->__client;
$sig="@{$this->__signature}@$method";
$len=count($args);
$args2=array($this->__java);
for($i=0; $i<$len; $i++) {
switch(gettype($val=$args[$i])) {
case 'boolean': array_push($args2,$val); $sig.='@b'; break;
case 'integer': array_push($args2,$val); $sig.='@i'; break;
case 'double': array_push($args2,$val); $sig.='@d'; break;
case 'string': array_push($args2,htmlspecialchars($val,ENT_COMPAT)); $sig.='@s'; break;
case 'array':$sig="~INVALID"; break;
case 'object':
if($val instanceof java_JavaType) {
array_push($args2,$val->__java);
$sig.="@o{$val->__signature}";
}
else {
$sig="~INVALID";
}
break;
case 'resource': array_push($args2,$val); $sig.='@r'; break;
case 'NULL': array_push($args2,$val); $sig.='@N'; break;
case 'unknown type': array_push($args2,$val); $sig.='@u'; break;
default: throw new java_IllegalArgumentException($val);
}
}
if(array_key_exists($sig,$client->methodCache)) {
$cacheEntry=&$client->methodCache[$sig];
$client->sendBuffer.=$client->preparedToSendBuffer;
if(strlen($client->sendBuffer)>=JAVA_SEND_SIZE) {
if($client->protocol->handler->write($client->sendBuffer)<=0)
throw new java_IllegalStateException("Out of sync. Check backend log for details.");
$client->sendBuffer=null;
}
$client->preparedToSendBuffer=vsprintf($cacheEntry->fmt,$args2);
if($cacheEntry->resultVoid) {
$client->cancelProxyCreationTag +=1;
return null;
} else {
$result=clone($client->cachedJavaPrototype);
$result->__factory=$cacheEntry->factory;
$result->__java=++$client->asyncCtx;
$result->__signature=$cacheEntry->signature;
$result->__cancelProxyCreationTag=++$client->cancelProxyCreationTag;
return $result;
}
} else {
$client->currentCacheKey=$sig;
$retval=parent::__call($method,$args);
return $retval;
}
}
}
class java_InternalJava extends Java {
function java_InternalJava($proxy) {
$this->__delegate=$proxy;
$this->__java=$proxy->__java;
$this->__signature=$proxy->__signature;
$this->__client=$proxy->__client;
}
}
class java_class extends Java {
function java_class() {
$this->__client=__javaproxy_Client_getClient();
$args=func_get_args();
$name=array_shift($args);
if(is_array($name)) { $args=$name; $name=array_shift($args); }
$delegate=$this->__delegate=$this->__client->referenceObject($name,$args);
$this->__java=$delegate->__java;
$this->__signature=$delegate->__signature;
}
}
class JavaClass extends java_class{}
class java_exception extends Exception implements java_JavaType {
public $__serialID,$__java,$__client;
public $__delegate;
public $__signature;
public $__hasDeclaredExceptions;
function java_exception() {
$this->__client=__javaproxy_Client_getClient();
$args=func_get_args();
$name=array_shift($args);
if(is_array($name)) { $args=$name; $name=array_shift($args); }
if (count($args)==0)
Exception::__construct($name);
else
Exception::__construct($args[0]);
$delegate=$this->__delegate=$this->__client->createObject($name,$args);
$this->__java=$delegate->__java;
$this->__signature=$delegate->__signature;
$this->__hasDeclaredExceptions='T';
}
function __cast($type) {
return $this->__delegate->__cast($type);
}
function __sleep() {
$this->__delegate->__sleep();
return array("__delegate");
}
function __wakeup() {
$this->__delegate->__wakeup();
$this->__java=$this->__delegate->__java;
$this->__client=$this->__delegate->__client;
}
function __get($key) {
return $this->__delegate->__get($key);
}
function __set($key,$val) {
$this->__delegate->__set($key,$val);
}
function __call($method,$args) {
return $this->__delegate->__call($method,$args);
}
function __toString() {
return $this->__delegate->__toExceptionString($this->getTraceAsString());
}
}
class JavaException extends java_exception {}
class java_InternalException extends JavaException {
function java_InternalException($proxy,$exception) {
$this->__delegate=$proxy;
$this->__java=$proxy->__java;
$this->__signature=$proxy->__signature;
$this->__client=$proxy->__client;
$this->__hasDeclaredExceptions=$exception;
}
}
class java_JavaProxyProxy extends Java {
function java_JavaProxyProxy($client) {
$this->__client=$client;
}
}
}
?>
