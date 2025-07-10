// processos.js

document.addEventListener('DOMContentLoaded', () => {

    // --- FONTE DE DADOS (Mock Data) ---
    let todosProcessos = [
        { id: 'PROC-101', nome: 'Implantação de Sistema', status: 'Em Andamento', cliente: { nome: 'TecnoCorp', id: 'CLI-001', responsavel: 'Carlos Silva' } },
        { id: 'PROC-102', nome: 'Consultoria de TI', status: 'Concluído', cliente: { nome: 'TecnoCorp', id: 'CLI-001', responsavel: 'Carlos Silva' } },
        { id: 'PROC-103', nome: 'Otimização de Processos', status: 'Em Andamento', cliente: { nome: 'Soluções Alfa', id: 'CLI-002', responsavel: 'Beatriz Costa' } },
        { id: 'PROC-104', nome: 'Desenvolvimento de App', status: 'Pausado', cliente: { nome: 'Inovações Gama', id: 'CLI-004', responsavel: 'Fernanda Lima' } },
        { id: 'PROC-105', nome: 'Auditoria de Segurança', status: 'Concluído', cliente: { nome: 'Grupo Beta', id: 'CLI-003', responsavel: 'Daniel Martins' } },
    ];

    // --- ESTADO DA APLICAÇÃO ---
    let currentPage = 1;
    const itemsPerPage = 10;
    let searchTerm = '';
    let currentProcessBeingViewedId = null;
    let currentClientFilter = 'all';
    let currentStatusFilter = 'all';
    let currentSort = 'name-asc';

    // --- REFERÊNCIAS AOS ELEMENTOS DO DOM ---
    const DOMElements = {
        searchInput: document.getElementById('search-input'),
        filterClientSelect: document.getElementById('filter-client-select'),
        filterStatusSelect: document.getElementById('filter-status-select'),
        sortProcessesSelect: document.getElementById('sort-processes-select'),
        processList: document.getElementById('process-list'),
        prevButton: document.getElementById('prev-button'),
        nextButton: document.getElementById('next-button'),
        pageInfo: document.getElementById('page-info'),
        addProcessBtn: document.getElementById('add-process-btn'),

        // Modal de Detalhes
        detailsModal: document.getElementById('process-details-modal'),
        closeDetailsModalBtn: document.getElementById('close-details-modal-btn'),
        deleteProcessBtn: document.getElementById('delete-process-btn'),
        accessProcessBtn: document.getElementById('access-process-btn'),
        detailProcessoNome: document.getElementById('detail-processo-nome'),
        detailProcessoId: document.getElementById('detail-processo-id'),
        detailProcessoStatus: document.getElementById('detail-processo-status'),
        detailClienteNome: document.getElementById('detail-cliente-nome'),
        detailClienteResponsavel: document.getElementById('detail-cliente-responsavel'),

        // NOVO: Elementos do Modal de Adicionar
        addProcessModal: document.getElementById('add-process-modal'),
        addProcessForm: document.getElementById('add-process-form'),
        closeAddModalBtn: document.getElementById('close-add-modal-btn'),
        cancelAddProcessBtn: document.getElementById('cancel-add-process-btn'),
        newProcessClientSelect: document.getElementById('new-process-client-select'),
    };

    // --- FUNÇÕES ---

    function populateClientFilter() {
        const clientes = [...new Map(todosProcessos.map(p => [p.cliente.id, p.cliente])).values()];
        clientes.sort((a, b) => a.nome.localeCompare(b.nome));
        DOMElements.filterClientSelect.innerHTML = '<option value="all">Todos os Clientes</option>';
        clientes.forEach(cliente => {
            const option = document.createElement('option');
            option.value = cliente.id;
            option.textContent = cliente.nome;
            DOMElements.filterClientSelect.appendChild(option);
        });
    }

    function render() {
        // (O conteúdo da função render permanece o mesmo)
        DOMElements.processList.innerHTML = '';
        let processedData = todosProcessos
            .filter(proc => proc.nome.toLowerCase().includes(searchTerm.toLowerCase()) || proc.cliente.nome.toLowerCase().includes(searchTerm.toLowerCase()))
            .filter(proc => currentClientFilter === 'all' || proc.cliente.id === currentClientFilter)
            .filter(proc => currentStatusFilter === 'all' || proc.status === currentStatusFilter);
        switch (currentSort) {
            case 'name-asc': processedData.sort((a, b) => a.nome.localeCompare(b.nome)); break;
            case 'name-desc': processedData.sort((a, b) => b.nome.localeCompare(a.nome)); break;
            case 'client-asc': processedData.sort((a, b) => a.cliente.nome.localeCompare(b.cliente.nome)); break;
            case 'client-desc': processedData.sort((a, b) => b.cliente.nome.localeCompare(a.cliente.nome)); break;
        }
        const paginatedProcessos = processedData.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage);
        if (paginatedProcessos.length > 0) {
            const processosHTML = paginatedProcessos.map(proc => {
                const statusClass = `status-${proc.status.toLowerCase().replace(' ', '-')}`;
                return `<li class="process-list-item" data-process-id="${proc.id}"><span class="process-name">${proc.nome}</span><span class="process-client-name">${proc.cliente.nome}</span><span class="process-status ${statusClass}">${proc.status}</span></li>`;
            }).join('');
            DOMElements.processList.innerHTML = processosHTML;
        } else {
            DOMElements.processList.innerHTML = `<li class="no-results-item">Nenhum processo encontrado.</li>`;
        }
        updatePaginationControls(processedData.length);
    }

    function updatePaginationControls(totalItems) {
        const totalPages = Math.ceil(totalItems / itemsPerPage) || 1;
        DOMElements.pageInfo.textContent = `Página ${currentPage} de ${totalPages}`;
        DOMElements.prevButton.disabled = currentPage === 1;
        DOMElements.nextButton.disabled = currentPage >= totalPages;
    }
    
    function showProcessDetails(processId) { const processo = todosProcessos.find(p => p.id === processId); if (!processo) return; currentProcessBeingViewedId = processo.id; const { nome, id, status, cliente } = processo; DOMElements.detailProcessoNome.textContent = nome; DOMElements.detailProcessoId.textContent = id; DOMElements.detailProcessoStatus.innerHTML = `<span class="process-status status-${status.toLowerCase().replace(' ', '-')}">${status}</span>`; DOMElements.detailClienteNome.textContent = cliente.nome; DOMElements.detailClienteResponsavel.textContent = cliente.responsavel; toggleModal(DOMElements.detailsModal, true); }
    function handleDeleteProcess() { if (!currentProcessBeingViewedId) return; const processoParaExcluir = todosProcessos.find(p => p.id === currentProcessBeingViewedId); if (!processoParaExcluir) return; const isConfirmed = confirm(`Você tem certeza que deseja excluir o processo "${processoParaExcluir.nome}"?`); if (isConfirmed) { todosProcessos = todosProcessos.filter(p => p.id !== currentProcessBeingViewedId); alert(`Processo "${processoParaExcluir.nome}" excluído.`); toggleModal(DOMElements.detailsModal, false); render(); } }
    function accessProcess() { if (!currentProcessBeingViewedId) return; window.location.href = `kanban.html?id=${currentProcessBeingViewedId}`; }
    function toggleModal(modal, show) { modal.classList.toggle('show', show); if (!show) currentProcessBeingViewedId = null; }

    // --- MODIFICADO: Lógica para o novo modal ---

    /**
     * NOVO: Popula a lista de clientes dentro do modal de adição.
     */
    function populateNewProcessClientSelect() {
        const select = DOMElements.newProcessClientSelect;
        select.innerHTML = '<option value="">Selecione um cliente...</option>'; // Opção padrão

        const clientes = [...new Map(todosProcessos.map(p => [p.cliente.id, p.cliente])).values()];
        clientes.sort((a, b) => a.nome.localeCompare(b.nome));
        
        clientes.forEach(cliente => {
            const option = document.createElement('option');
            option.value = cliente.id;
            option.textContent = cliente.nome;
            select.appendChild(option);
        });
    }

    /**
     * MODIFICADO: Agora esta função apenas abre e prepara o modal.
     */
    function handleAddProcess() {
        populateNewProcessClientSelect(); // Popula a lista de clientes
        toggleModal(DOMElements.addProcessModal, true); // Mostra o modal
    }

    /**
     * NOVO: Salva o novo processo a partir dos dados do formulário do modal.
     */
    function handleSaveNewProcess(event) {
        event.preventDefault(); // Impede o recarregamento da página

        const nome = DOMElements.addProcessForm.elements['new-process-name'].value.trim();
        const clienteId = DOMElements.addProcessForm.elements['new-process-client-select'].value;

        if (!nome || !clienteId) {
            alert('Por favor, preencha todos os campos obrigatórios.');
            return;
        }

        // Encontra o objeto completo do cliente para obter o nome e responsável
        const clienteSelecionado = todosProcessos.find(p => p.cliente.id === clienteId).cliente;

        const novoProcesso = {
            id: 'PROC-' + Date.now(),
            nome: nome,
            status: 'Não Iniciado',
            cliente: {
                id: clienteSelecionado.id,
                nome: clienteSelecionado.nome,
                responsavel: clienteSelecionado.responsavel,
            }
        };
        
        todosProcessos.unshift(novoProcesso);
        alert(`Processo "${nome}" criado com sucesso!`);
        
        toggleModal(DOMElements.addProcessModal, false); // Esconde o modal
        DOMElements.addProcessForm.reset(); // Limpa o formulário
        
        currentPage = 1;
        render();
    }


    function bindEventListeners() {
        // Listener do botão principal para abrir o modal
        DOMElements.addProcessBtn.addEventListener('click', handleAddProcess);

        // NOVO: Listeners para o modal de adição
        DOMElements.addProcessForm.addEventListener('submit', handleSaveNewProcess);
        DOMElements.closeAddModalBtn.addEventListener('click', () => toggleModal(DOMElements.addProcessModal, false));
        DOMElements.cancelAddProcessBtn.addEventListener('click', () => toggleModal(DOMElements.addProcessModal, false));
        DOMElements.addProcessModal.addEventListener('click', (event) => {
            if (event.target === DOMElements.addProcessModal) {
                toggleModal(DOMElements.addProcessModal, false);
            }
        });

        // Listeners existentes...
        DOMElements.searchInput.addEventListener('input', (event) => { searchTerm = event.target.value; currentPage = 1; render(); });
        DOMElements.filterClientSelect.addEventListener('change', (event) => { currentClientFilter = event.target.value; currentPage = 1; render(); });
        DOMElements.filterStatusSelect.addEventListener('change', (event) => { currentStatusFilter = event.target.value; currentPage = 1; render(); });
        DOMElements.sortProcessesSelect.addEventListener('change', (event) => { currentSort = event.target.value; currentPage = 1; render(); });
        DOMElements.prevButton.addEventListener('click', () => { if(currentPage > 1) {currentPage--; render();} });
        DOMElements.nextButton.addEventListener('click', () => { currentPage++; render(); });
        DOMElements.processList.addEventListener('click', (event) => { const processItem = event.target.closest('.process-list-item'); if (processItem) showProcessDetails(processItem.dataset.processId); });
        DOMElements.closeDetailsModalBtn.addEventListener('click', () => toggleModal(DOMElements.detailsModal, false));
        DOMElements.detailsModal.addEventListener('click', (event) => { if (event.target === DOMElements.detailsModal) toggleModal(DOMElements.detailsModal, false); });
        DOMElements.deleteProcessBtn.addEventListener('click', handleDeleteProcess);
        DOMElements.accessProcessBtn.addEventListener('click', accessProcess);
    }

    function init() {
        populateClientFilter();
        bindEventListeners();
        render();
    }

    // Inicia a aplicação
    init();
});