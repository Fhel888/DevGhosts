// processos.js

document.addEventListener('DOMContentLoaded', () => {

    // --- FONTE DE DADOS (Mock Data) ---
    let todosProcessos = [
        { id: 'PROC-101', nome: 'Implantação de Sistema', status: 'Em Andamento', setor: 'TI', cliente: { nome: 'TecnoCorp', id: 'CLI-001', responsavel: 'Carlos Silva' } },
        { id: 'PROC-102', nome: 'Consultoria de TI', status: 'Concluído', setor: 'TI', cliente: { nome: 'TecnoCorp', id: 'CLI-001', responsavel: 'Carlos Silva' } },
        { id: 'PROC-103', nome: 'Otimização de Processos', status: 'Em Andamento', setor: 'Operações', cliente: { nome: 'Soluções Alfa', id: 'CLI-002', responsavel: 'Beatriz Costa' } },
        { id: 'PROC-104', nome: 'Desenvolvimento de App', status: 'Pausado', setor: 'Desenvolvimento', cliente: { nome: 'Inovações Gama', id: 'CLI-004', responsavel: 'Fernanda Lima' } },
        { id: 'PROC-105', nome: 'Auditoria de Segurança', status: 'Concluído', setor: 'Financeiro', cliente: { nome: 'Grupo Beta', id: 'CLI-003', responsavel: 'Daniel Martins' } },
        { id: 'PROC-106', nome: 'Recrutamento de Vagas', status: 'Em Andamento', setor: 'RH', cliente: { nome: 'TecnoCorp', id: 'CLI-001', responsavel: 'Carlos Silva' } }
    ];

    // --- ESTADO DA APLICAÇÃO ---
    let currentPage = 1;
    const itemsPerPage = 10;
    let searchTerm = '';
    let currentProcessBeingViewedId = null;
    let currentSetorFilter = 'all';
    let currentStatusFilter = 'all';
    let currentSort = 'name-asc';

    // --- REFERÊNCIAS AOS ELEMENTOS DO DOM ---
    const DOMElements = {
        // Controles da Lista
        searchInput: document.getElementById('search-input'),
        filterSetorSelect: document.getElementById('filter-setor-select'),
        filterStatusSelect: document.getElementById('filter-status-select'),
        sortProcessesSelect: document.getElementById('sort-processes-select'),
        processList: document.getElementById('process-list'),
        
        // Paginação
        prevButton: document.getElementById('prev-button'),
        nextButton: document.getElementById('next-button'),
        pageInfo: document.getElementById('page-info'),
        
        // Modal de Adicionar Processo
        addProcessBtn: document.getElementById('add-process-btn'),
        addProcessModal: document.getElementById('add-process-modal'),
        addProcessForm: document.getElementById('add-process-form'),
        closeAddModalBtn: document.getElementById('close-add-modal-btn'),
        cancelAddProcessBtn: document.getElementById('cancel-add-process-btn'),
        newProcessClientSelect: document.getElementById('new-process-client-select'),
        newProcessSetorSelect: document.getElementById('new-process-setor-select'),

        // Modal de Detalhes
        detailsModal: document.getElementById('process-details-modal'),
        closeDetailsModalBtn: document.getElementById('close-details-modal-btn'),
        deleteProcessBtn: document.getElementById('delete-process-btn'),
        accessProcessBtn: document.getElementById('access-process-btn'),
        detailProcessoNome: document.getElementById('detail-processo-nome'),
        detailProcessoId: document.getElementById('detail-processo-id'),
        detailProcessoStatus: document.getElementById('detail-processo-status'),
        detailProcessoSetor: document.getElementById('detail-processo-setor'),
        detailClienteNome: document.getElementById('detail-cliente-nome'),
        detailClienteResponsavel: document.getElementById('detail-cliente-responsavel'),
    };

    // --- FUNÇÕES ---

    function populateSetorFilter() {
        const setores = [...new Set(todosProcessos.map(p => p.setor))].sort();
        DOMElements.filterSetorSelect.innerHTML = '<option value="all">Todos os Setores</option>';
        setores.forEach(setor => {
            const option = document.createElement('option');
            option.value = setor;
            option.textContent = setor;
            DOMElements.filterSetorSelect.appendChild(option);
        });
    }

    function render() {
        DOMElements.processList.innerHTML = '';
        
        let processedData = todosProcessos
            .filter(proc => proc.nome.toLowerCase().includes(searchTerm.toLowerCase()) || proc.cliente.nome.toLowerCase().includes(searchTerm.toLowerCase()))
            .filter(proc => currentSetorFilter === 'all' || proc.setor === currentSetorFilter)
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
                return `
                    <li class="process-list-item" data-process-id="${proc.id}">
                        <span class="process-name">${proc.nome}</span>
                        <span class="process-client-name">${proc.cliente.nome}</span>
                        <span class="process-status ${statusClass}">${proc.status}</span>
                    </li>`;
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
        DOMElements.nextButton.disabled = currentPage === totalPages;
    }
    
    function showProcessDetails(processId) {
        const processo = todosProcessos.find(p => p.id === processId);
        if (!processo) return;
        currentProcessBeingViewedId = processo.id;
        const { nome, id, status, cliente, setor } = processo;
        DOMElements.detailProcessoNome.textContent = nome;
        DOMElements.detailProcessoId.textContent = id;
        DOMElements.detailProcessoStatus.innerHTML = `<span class="process-status status-${status.toLowerCase().replace(' ', '-')}">${status}</span>`;
        DOMElements.detailProcessoSetor.textContent = setor;
        DOMElements.detailClienteNome.textContent = cliente.nome;
        DOMElements.detailClienteResponsavel.textContent = cliente.responsavel;
        toggleModal(DOMElements.detailsModal, true);
    }
    
    function handleDeleteProcess() {
        if (!currentProcessBeingViewedId) return;
        const processoParaExcluir = todosProcessos.find(p => p.id === currentProcessBeingViewedId);
        if (!processoParaExcluir) return;
        const isConfirmed = confirm(`Você tem certeza que deseja excluir o processo "${processoParaExcluir.nome}"?`);
        if (isConfirmed) {
            console.log(`ENVIANDO REQUISIÇÃO DELETE: /api/processos/${currentProcessBeingViewedId}`);
            todosProcessos = todosProcessos.filter(p => p.id !== currentProcessBeingViewedId);
            alert(`Processo "${processoParaExcluir.nome}" excluído.`);
            toggleModal(DOMElements.detailsModal, false);
            render();
        }
    }

    function accessProcess() {
        if (!currentProcessBeingViewedId) return;
        window.location.href = `kanban.html?id=${currentProcessBeingViewedId}`;
    }
    
    function populateAddProcessModalSelects() {
        const clientes = [...new Map(todosProcessos.map(p => [p.cliente.id, p.cliente])).values()].sort((a,b) => a.nome.localeCompare(b.nome));
        DOMElements.newProcessClientSelect.innerHTML = '<option value="" disabled selected>Selecione um cliente</option>';
        clientes.forEach(cliente => {
            const option = document.createElement('option');
            option.value = cliente.id;
            option.textContent = cliente.nome;
            DOMElements.newProcessClientSelect.appendChild(option);
        });

        const setores = [...new Set(todosProcessos.map(p => p.setor))].sort();
        DOMElements.newProcessSetorSelect.innerHTML = '<option value="" disabled selected>Selecione um setor</option>';
        setores.forEach(setor => {
            const option = document.createElement('option');
            option.value = setor;
            option.textContent = setor;
            DOMElements.newProcessSetorSelect.appendChild(option);
        });
    }

    function handleSaveNewProcess(event) {
        event.preventDefault();
        const nome = DOMElements.addProcessForm.elements['processName'].value.trim();
        const clienteId = DOMElements.addProcessForm.elements['clientId'].value;
        const setor = DOMElements.addProcessForm.elements['setor'].value;

        if (!nome || !clienteId || !setor) {
            alert('Por favor, preencha todos os campos obrigatórios.');
            return;
        }

        console.log(`NOVO PROCESSO (simulação): Nome: ${nome}, Cliente ID: ${clienteId}, Setor: ${setor}`);
        alert(`Processo "${nome}" criado com sucesso! (Simulação)`);
        
        toggleModal(DOMElements.addProcessModal, false);
    }

    function toggleModal(modal, show) {
        modal.classList.toggle('show', show);
        if (!show) {
            if (modal === DOMElements.detailsModal) {
                currentProcessBeingViewedId = null;
            } else if (modal === DOMElements.addProcessModal) {
                DOMElements.addProcessForm.reset();
            }
        }
    }

    function bindEventListeners() {
        DOMElements.searchInput.addEventListener('input', (event) => { searchTerm = event.target.value; currentPage = 1; render(); });
        DOMElements.filterSetorSelect.addEventListener('change', (event) => { currentSetorFilter = event.target.value; currentPage = 1; render(); });
        DOMElements.filterStatusSelect.addEventListener('change', (event) => { currentStatusFilter = event.target.value; currentPage = 1; render(); });
        DOMElements.sortProcessesSelect.addEventListener('change', (event) => { currentSort = event.target.value; currentPage = 1; render(); });
        
        DOMElements.prevButton.addEventListener('click', () => { if(currentPage > 1) {currentPage--; render();} });
        DOMElements.nextButton.addEventListener('click', () => {
            const totalPages = Math.ceil(todosProcessos.length / itemsPerPage);
            if (currentPage < totalPages) { currentPage++; render(); }
        });
        
        DOMElements.processList.addEventListener('click', (event) => {
            const processItem = event.target.closest('.process-list-item');
            if (processItem) showProcessDetails(processItem.dataset.processId);
        });

        // Listeners do Modal de Adicionar
        DOMElements.addProcessBtn.addEventListener('click', () => {
            populateAddProcessModalSelects();
            toggleModal(DOMElements.addProcessModal, true);
        });
        DOMElements.addProcessForm.addEventListener('submit', handleSaveNewProcess);
        DOMElements.closeAddModalBtn.addEventListener('click', () => toggleModal(DOMElements.addProcessModal, false));
        DOMElements.cancelAddProcessBtn.addEventListener('click', () => toggleModal(DOMElements.addProcessModal, false));
        DOMElements.addProcessModal.addEventListener('click', (event) => {
            if (event.target === DOMElements.addProcessModal) toggleModal(DOMElements.addProcessModal, false);
        });
        
        // Listeners do Modal de Detalhes
        DOMElements.closeDetailsModalBtn.addEventListener('click', () => toggleModal(DOMElements.detailsModal, false));
        DOMElements.detailsModal.addEventListener('click', (event) => {
            if (event.target === DOMElements.detailsModal) toggleModal(DOMElements.detailsModal, false);
        });
        DOMElements.deleteProcessBtn.addEventListener('click', handleDeleteProcess);
        DOMElements.accessProcessBtn.addEventListener('click', accessProcess);
    }

    function init() {
        populateSetorFilter();
        bindEventListeners();
        render();
    }

    init();
});