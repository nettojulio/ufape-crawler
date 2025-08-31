import ForceGraph3D from 'https://esm.sh/react-force-graph-3d?external=react';
import React from 'react';
import { createRoot } from 'react-dom/client';
import SpriteText from "https://esm.sh/three-spritetext";

fetch('./grafo_salvo.json')
    .then(res => res.json())
    .then(originalData => {
        let maxDepth = Number.MAX_SAFE_INTEGER;

        const filteredNodes = originalData.nodes.filter(node => node.depth <= maxDepth);
        const validNodeIds = new Set(filteredNodes.map(node => node.id));

        const filteredLinks = originalData.links.filter(link =>
            validNodeIds.has(link.source) && validNodeIds.has(link.target)
        );

        const filteredGraphData = {
            nodes: filteredNodes,
            links: filteredLinks
        };

        const container = document.getElementById('graph');
        const root = createRoot(container);

        root.render(
            <ForceGraph3D
                graphData={filteredGraphData}
                nodeAutoColorBy="statusCode"
                nodeLabel={node => `<b>${node.id}</b>: ${node.depth} | ${node.statusCode}`}
                nodeThreeObjectExtend={true}
                linkDirectionalParticles={1}
                linkDirectionalParticleSpeed={0.01}
            />
        );
    })
    .catch(error => {
        console.error("Erro ao carregar ou processar o arquivo JSON:", error);
        document.getElementById('graph').textContent = 'Falha ao carregar os dados do grafo.';
    });