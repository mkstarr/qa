<?php
class MyClass
{
	const constant = 'constant value';
	public $vari = 3.1415;
	function showConstant() {
		echo  self::constant . "\n";
	}
}

const CONSTANT = 'Hello world';
$array = array(1, 2, 3, 4, 5);
print_r($array);

function foo(){
	static $int = 0;
	$int++;
	echo $int;
}

?>