<?php
include 'conexao.php';

header('Content-Type: application/json');

// Filtro por setor (via GET)
$setor = $_GET['setor'] ?? null;

try {
    if ($setor) {
        $stmt = $pdo->prepare("SELECT * FROM processos WHERE setor = ? ORDER BY data_upload DESC");
        $stmt->execute([$setor]);
    } else {
        $stmt = $pdo->query("SELECT * FROM processos ORDER BY data_upload DESC");
    }

    $processos = $stmt->fetchAll(PDO::FETCH_ASSOC);
    echo json_encode($processos);

} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["erro" => "Erro ao buscar dados."]);
}
