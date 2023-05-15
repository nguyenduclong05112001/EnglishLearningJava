<?php
class Apps_Libs_General{
    public function encodePass($pass) {
        return md5($pass);
    }
}
