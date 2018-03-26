<?php 
/* wrapper for Java.inc */ 

if(!function_exists("java_get_base")) require_once("Java.inc"); 

if ($java_script_orig = $java_script = java_getHeader("X_JAVABRIDGE_INCLUDE", $_SERVER)) {

  if ($java_script!="@") {
    if (($_SERVER['REMOTE_ADDR']=='127.0.0.1') || (($java_script = realpath($java_script)) && (!strncmp($_SERVER['DOCUMENT_ROOT'], $java_script, strlen($_SERVER['DOCUMENT_ROOT']))))) {
      chdir (dirname ($java_script));
      require_once($java_script);
    } else {
      trigger_error("illegal access: ".$java_script_orig, E_USER_ERROR);
    }
  }

  java_call_with_continuation();
}
?>
