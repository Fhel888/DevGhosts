<?php
// Configura o fuso horário para o Brasil
date_default_timezone_set('America/Sao_Paulo');

// Conexão com o banco de dados
$pdo = new PDO('mysql:host=db;dbname=erp', 'erpuser', 'senha123');

// Configura o fuso horário na sessão MySQL também
$pdo->exec("SET time_zone = '-03:00'");
?>
