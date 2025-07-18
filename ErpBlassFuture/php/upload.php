<?php
include 'conexao.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
  $nome = $_POST['nome'];
  $setor = $_POST['setor'];
  $arquivo_info = $_FILES['arquivo'];

  // Gera um nome de arquivo único para evitar conflitos e problemas de segurança
  $nome_arquivo_original = basename($arquivo_info['name']);
  $extensao = pathinfo($nome_arquivo_original, PATHINFO_EXTENSION);
  $nome_arquivo_seguro = uniqid('proc_', true) . '.' . $extensao;
  
  $caminho_destino = "uploads/" . $nome_arquivo_seguro;

  if (move_uploaded_file($arquivo_info['tmp_name'], $caminho_destino)) {
    // Pega a data/hora atual no fuso horário configurado
    $data_atual = date('Y-m-d H:i:s');
    
    $stmt = $pdo->prepare("INSERT INTO processos (nome, setor, arquivo, data_upload) VALUES (?, ?, ?, ?)");
    $stmt->execute([$nome, $setor, $nome_arquivo_seguro, $data_atual]);
    echo "<h3>Upload realizado com sucesso!</h3>";
  } else {
    echo "<h3>Falha no upload do arquivo.</h3>";
  }
}
?>
<a href=index.php>Voltar</a>
