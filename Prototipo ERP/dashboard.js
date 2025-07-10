// dashboard.js

document.addEventListener('DOMContentLoaded', () => {

    // --- FONTE DE DADOS (Mock Data) ---
    const todosProcessos = [
        { id: 'PROC-101', nome: 'Implantação de Sistema', status: 'Em Andamento', dataInicio: '2025-07-01', cliente: { nome: 'TecnoCorp' } },
        { id: 'PROC-102', nome: 'Consultoria de TI', status: 'Concluído', dataInicio: '2025-06-20', cliente: { nome: 'TecnoCorp' } },
        { id: 'PROC-103', nome: 'Otimização de Processos', status: 'Em Andamento', dataInicio: '2025-07-05', cliente: { nome: 'Soluções Alfa' } },
        { id: 'PROC-104', nome: 'Desenvolvimento de App', status: 'Pausado', dataInicio: '2025-05-10', cliente: { nome: 'Inovações Gama' } },
        { id: 'PROC-105', nome: 'Auditoria de Segurança', status: 'Concluído', dataInicio: '2025-07-02', cliente: { nome: 'Grupo Beta' } },
        { id: 'PROC-106', nome: 'Migração de Servidores', status: 'Em Andamento', dataInicio: '2025-06-28', cliente: { nome: 'Nexus Systems' } },
        { id: 'PROC-107', nome: 'Treinamento de Equipe', status: 'Concluído', dataInicio: '2025-06-15', cliente: { nome: 'Soluções Alfa' } },
        { id: 'PROC-108', nome: 'Manutenção de Legado', status: 'Pausado', dataInicio: '2025-04-30', cliente: { nome: 'TecnoCorp' } },
        { id: 'PROC-109', nome: 'Criação de Website', status: 'Em Andamento', dataInicio: '2025-07-08', cliente: { nome: 'Apex Indústrias' } },
        { id: 'PROC-110', nome: 'Análise de Dados', status: 'Em Andamento', dataInicio: '2025-07-06', cliente: { nome: 'Quantum Solutions' } },
        { id: 'PROC-111', nome: 'Revisão de Contratos', status: 'Concluído', dataInicio: '2025-07-04', cliente: { nome: 'Grupo Beta' } },
    ];

    // --- REFERÊNCIAS AOS ELEMENTOS DO DOM ---
    const andamentoList = document.getElementById('andamento-list');
    const concluidosList = document.getElementById('concluidos-list');
    const pausadosList = document.getElementById('pausados-list');

    /**
     * Filtra, ordena e pega os 5 processos mais recentes de um determinado status.
     */
    function getRecentProcessesByStatus(status) {
        return todosProcessos
            .filter(proc => proc.status === status)
            .sort((a, b) => new Date(b.dataInicio) - new Date(a.dataInicio))
            .slice(0, 5);
    }

    /**
     * ATUALIZADO: Renderiza uma lista de processos clicáveis.
     * @param {HTMLElement} listElement - O elemento <ul> onde a lista será renderizada.
     * @param {Array} processData - Os dados dos processos a serem exibidos.
     */
    function populateList(listElement, processData) {
        listElement.innerHTML = '';

        if (processData.length === 0) {
            listElement.innerHTML = '<li class="dashboard-list-item">Nenhum processo encontrado.</li>';
            return;
        }

        // ATUALIZAÇÃO: O conteúdo do <li> agora é envolvido por uma tag <a>
        const itemsHTML = processData.map(proc => `
            <li class="dashboard-list-item">
                <a href="processo-detalhe.html?id=${proc.id}" class="dashboard-item-link">
                    <span class="item-process-name">${proc.nome}</span>
                    <span class="item-client-name">${proc.cliente.nome}</span>
                </a>
            </li>
        `).join('');

        listElement.innerHTML = itemsHTML;
    }

    // --- INICIALIZAÇÃO ---
    populateList(andamentoList, getRecentProcessesByStatus('Em Andamento'));
    populateList(concluidosList, getRecentProcessesByStatus('Concluído'));
    populateList(pausadosList, getRecentProcessesByStatus('Pausado'));
});