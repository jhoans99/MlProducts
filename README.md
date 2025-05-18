# MlProducts
Reto Tecnico para ingreso de mercado libre como desarrollador Android

La aplicación se estructuró utilizando un enfoque multi-módulo con el objetivo de lograr una separación clara de responsabilidades, escalabilidad y mantenibilidad del código., la cual cuenta con el modulo app, core, features, domian y el modulo data

En el modulo app se encuentra la configuración de permisos, dagger Hilt y el contralador de navegación.
En el core se encuentran elementos  que son transversales a la aplicación, como los elementos de ui, el builder de retrofit y una clase Result, la cual se usa para manejar los estados
de acuerdo a la respuesta de retrfoit donde se logra emitit un estado para Loading success y error que serán escuchados en los viewmodel

El modulo feautures contiene las tres pantallas, cada submodulo de features contiene su ui State, viewmodel y su achivo composable. En donde ninguna funcionanlidad se conocé entre sí permitiendo la separación de capas. Las archivos composable cuentan con sus funciones separadas para un mejor mantenimiento, tambien se encuentran los viewmodel para gestionar el estado de la vista y gestionar los datos solicitados para mostrar al usuario. Y por el ultimo se tiene los ui State. Según las necesidades de cada funcionalidad.

El modulo domain se encuntra la interfaz del repository, para esta prueba no se implementaron UseCase, porqué no se tiene un logica de negocio tan espefica para hacer uso de los useCase. Se crean las interfaces de repository en este punto para garantizar que la capa de Ui logre conocer los metodos que debe llamar el viewmodel para obtener los datos de productos.

El modulo data contiene los paquetes repository y dataSource, se utilizaron estas dos clases ya que el datasource permite separar las depedencias externas que existan en el proyecto y a futuro pueda ser modificable y escalable, estas depedencias son las relacionadas con retrofit y DataStore. Por lo cual si se desea cambiar DataStore por SharedPrefence o por Room se podria realizar sin modificar la logica del repository o del viewmodel si no solo cambiar la implementación del DataSource. Por su lado el repository se encargar de solicitar los datos y generar los Flow y emitir el Result para los viewmodel y tener la decisión si obtener los datos de remoto y de local

Tecnologias del proyecto:

Ui: Jetpack Compose
State : StateFlow
DI: Dagger Hilt
Network: Retrofit
Local: DataStore
Async Task: Corrutines
Testing: Unit Test(Run a next coomand ./gradlew test)
