<?php
include_once 'Database.php';

class DatabaseMySql implements Database {
    
    public $conn;
    
    function __construct($host, $db, $username, $password) {
        try {
            $this->conn = new PDO("mysql:host=" . $host . ";dbname=" . $db, $username, $password);
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->conn->exec("set names utf8");
            
        } catch(PDOException $e){
            throw new Exception('Connection error: ' . $e->getMessage(), 0, $e);
        }
    }
 
    public function getConnection() {
        return $this->conn;
    }
}