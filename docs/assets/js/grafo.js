import React from 'react';
import { createRoot } from 'react-dom/client';
import ForceGraph3D from 'react-force-graph-3d';
import SpriteText from 'three-spritetext';

fetch('./assets/data/grafo_salvo.json')
    .then(res => res.json())
    .then(data => {

        let maxDepth = Number.MAX_SAFE_INTEGER;

        const filteredNodes = data.nodes.filter(node => node.depth <= maxDepth);
        const validNodeIds = new Set(filteredNodes.map(node => node.id));

        const filteredLinks = data.links.filter(link =>
            validNodeIds.has(link.source) && validNodeIds.has(link.target)
        );

        const filteredGraphData = {
            nodes: filteredNodes,
            links: filteredLinks
        };

        const container = document.getElementById('graph-container');
        const root = createRoot(container);
        root.render(
            <ForceGraph3D
                graphData={filteredGraphData}
                nodeAutoColorBy="statusCode"
                nodeLabel={node => `URL: ${node.id}\nDepth: ${node.depth}\nStatus: ${node.statusCode}`}
                nodeThreeObjectExtend={true}
                linkDirectionalParticles={1}
                linkDirectionalParticleSpeed={0.01}
            />
        );
    })
    .catch(error => {
        console.error("Erro ao carregar o arquivo JSON:", error);
        document.getElementById('graph-container').innerHTML = '<h2 style="color: red; text-align: center;">Falha ao carregar dados do grafo.</h2>';
    });