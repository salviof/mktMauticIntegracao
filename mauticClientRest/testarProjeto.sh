DIRETORIO_PROJETO=`pwd` # significa: print working directory ;)
NOME_SCRIPT=${0##*/} 
echo "Executando $NOME_SCRIPT Projeto em: $DIRETORIO_PROJETO"
/home/superBits/superBitsDevOps/devOpsProjeto/executarAcao.sh $DIRETORIO_PROJETO $NOME_SCRIPT
