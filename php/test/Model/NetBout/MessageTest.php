<?php
/**
 * @version $Id$
 */

require_once 'FaZend/Test/TestCase.php';

class Model_NetBout_MessageTest extends FaZend_Test_TestCase
{
    public function testClassExists()
    {
        $this->assertTrue(class_exists(substr(get_class(), 0, -4)));
    }
}
