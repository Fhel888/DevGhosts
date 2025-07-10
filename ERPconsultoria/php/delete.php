<?php
include 'conexao.php';

$id = $_GET['id'];
$stmt = $pdo->prepare("SELECT arquivo FROM processos WHERE id = ?");
$stmt->execute([$id]);
$arquivo = $stmt->fetchColumn();

unlink("uploads/" . $arquivo);

$pdo->prepare("DELETE FROM processos WHERE id = ?")->execute([$id]);

header("Location: index.php");
?>
