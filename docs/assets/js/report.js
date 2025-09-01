const totalLinksSpan = document.getElementById('total-links');
const errorListContainer = document.getElementById('error-list-container');
const successListContainer = document.getElementById('success-list-container');

const brokenLinksCountSpan = document.getElementById('broken-links-count');
const successLinksCountSpan = document.getElementById('success-links-count');

const modal = document.getElementById('path-modal');
const closeModalBtn = document.querySelector('.close-btn');
const pathDisplay = document.getElementById('path-display');
let graphData = null;

function findPath(startId, endId, graph) {
    const rootNode = graph.nodes.find(n => n.depth === 1);
    if (!rootNode) return null;
    startId = rootNode.id;

    if (!startId || !endId || !graph) return null;

    const adj = new Map();
    graph.links.forEach(link => {
        if (!adj.has(link.source)) adj.set(link.source, []);
        adj.get(link.source).push(link.target);
    });

    const queue = [startId];
    const visited = new Set([startId]);
    const predecessor = new Map();

    while (queue.length > 0) {
        const currentNodeId = queue.shift();
        if (currentNodeId === endId) {
            const path = [];
            let step = endId;
            while (step) {
                path.push(step);
                step = predecessor.get(step);
            }
            return path.reverse();
        }
        const neighbors = adj.get(currentNodeId) || [];
        for (const neighborId of neighbors) {
            if (!visited.has(neighborId)) {
                visited.add(neighborId);
                predecessor.set(neighborId, currentNodeId);
                queue.push(neighborId);
            }
        }
    }
    return null;
}

closeModalBtn.onclick = () => modal.style.display = 'none';
window.onclick = (event) => {
    if (event.target == modal) modal.style.display = 'none';
};

function handleViewPathClick(event) {
    if (event.target.classList.contains('view-path-btn')) {
        const targetUrl = event.target.dataset.targetUrl;
        const path = findPath(null, targetUrl, graphData);

        pathDisplay.innerHTML = '';
        if (path) {
            const listElement = document.createElement('ol');
            path.forEach(url => {
                const stepElement = document.createElement('li');
                stepElement.textContent = url;
                listElement.appendChild(stepElement);
            });
            pathDisplay.appendChild(listElement);
        } else {
            pathDisplay.textContent = 'O caminho não pôde ser determinado (o link pode ter sido originado de um redirecionamento ou de uma página externa não rastreada).';
        }
        modal.style.display = 'flex';
    }
}

errorListContainer.addEventListener('click', handleViewPathClick);

successListContainer.addEventListener('click', handleViewPathClick);

fetch('./assets/data/grafo_salvo.json')
    .then(res => res.json())
    .then(data => {
        graphData = data;

        const totalLinks = data.nodes.length;
        const brokenLinks = data.nodes.filter(node => node.statusCode === 404);
        const successLinks = data.nodes.filter(node => node.statusCode === 200);

        totalLinksSpan.textContent = totalLinks;

        brokenLinksCountSpan.textContent = `(Total de links problemáticos: ${brokenLinks.length})`;

        errorListContainer.innerHTML = '';
        if (brokenLinks.length > 0) {
            brokenLinks.forEach(link => {
                const itemContainer = document.createElement('div');
                itemContainer.className = 'error-item';

                const urlSpan = document.createElement('span');
                urlSpan.textContent = link.id;

                const pathButton = document.createElement('button');
                pathButton.className = 'view-path-btn';
                pathButton.textContent = 'Ver Caminho';
                pathButton.dataset.targetUrl = link.id;

                itemContainer.appendChild(urlSpan);
                itemContainer.appendChild(pathButton);
                errorListContainer.appendChild(itemContainer);
            });
        } else {
            errorListContainer.innerHTML = '<p>Nenhum link com status 404 foi encontrado.</p>';
        }

        successListContainer.innerHTML = '';
        if (successLinks.length > 0) {
            // NOVO: Cria a lista de sucesso com o botão "Ver Caminho"
            successLinks.forEach(link => {
                const itemContainer = document.createElement('div');
                itemContainer.className = 'success-item';
                const urlSpan = document.createElement('span');
                urlSpan.textContent = link.id;
                const pathButton = document.createElement('button');
                pathButton.className = 'view-path-btn';
                pathButton.textContent = 'Ver Caminho';
                pathButton.dataset.targetUrl = link.id;
                itemContainer.appendChild(urlSpan);
                itemContainer.appendChild(pathButton);
                successListContainer.appendChild(itemContainer);
            });
        } else {
            successListContainer.innerHTML = '<p>Nenhum link com status 200 foi encontrado.</p>';
        }
    })
    .catch(error => {
        console.error("Erro ao carregar o arquivo JSON:", error);
        totalLinksSpan.textContent = "Erro";
        const errorMessage = '<p>Não foi possível carregar o arquivo <strong>grafo_salvo.json</strong>. Verifique se o arquivo existe na pasta public.</p>';
        errorListContainer.innerHTML = errorMessage;
        successListContainer.innerHTML = errorMessage;
    });