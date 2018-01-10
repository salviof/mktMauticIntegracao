DIRETORIO_PROJETO=`pwd`
DIRETORIO_WEBAPP_SERVLET=$DIRETORIO_PROJETO/src/main/webapp
DIRETORIO_RESOURCES=$DIRETORIO_PROJETO/src/main/webapp/resources
if [ ! -d $DIRETORIO_WEBAPP_SERVLET ]; then
  echo "Diretorio webApp não existe, certifique que este é um projeto web"
  exit 1
fi
if [ ! -d $DIRETORIO_WEBAPP_SERVLET ]; then
  echo "Diretorio resources não existe, certifique que este é um projeto web"
  exit 1
fi
cd ..
rm $DIRETORIO_WEBAPP_SERVLET/WEB-INF -f -d
rm $DIRETORIO_RESOURCES/SBComp -f -d
git submodule add https://github.com/salviof/WEB-INF.git $DIRETORIO_WEBAPP_SERVLET/WEB-INF
git submodule add https://github.com/salviof/SBComp.git $DIRETORIO_RESOURCES/SBComp
#cd resources

