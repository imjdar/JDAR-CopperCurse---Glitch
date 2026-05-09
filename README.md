# JDAR-CopperCurse

Un plugin de desafío para Minecraft 1.21.x que impone una maldición de cobre sobre el mundo, limitando los recursos valiosos y expandiendo la utilidad del cobre.

## Características

### 1. Minería Restringida
- **Mina de Cobre Forzada**: Al picar Hierro, Oro, Carbón o Esmeralda, el drop se cancela y suelta Lingotes de Cobre o Cobre en Bruto.
- **Límite de Diamantes**: Los jugadores solo pueden picar exactamente **5 diamantes**. A partir del sexto, la mena se evapora y suelta cobre.
- **Lapis Lazuli**: Mantiene su drop normal.

### 2. Restricciones de Crafteo
- Se han deshabilitado los crafteos de armaduras y herramientas de:
  - Hierro
  - Oro
  - Diamante (Excepto el **Pico de Diamante**)
  - Netherite
- Los jugadores deben progresar usando Madera, Piedra y el nuevo Set de Cobre.

### 3. Equipamiento de Cobre Extendido
- **Set de Cobre Base**: Crafteos estándar usando lingotes de cobre. Tienen atributos intermedios y usan `CustomModelData`.
- **Equipamiento Pesado (Bloques)**: 
  - **Set Completo de Bloques**: Ahora puedes fabricar armaduras y herramientas completas usando **Bloques de Cobre**.
  - **Atributos Superiores**: Este set tiene mayor protección (Toughness) y daño (6.5 para la espada).
  - **Pasiva: Golpe Oxidante**: 15% de probabilidad de aplicar Lentitud y Fatiga de Minería al enemigo.
- **Escudo Glitch de Cobre**: 
  - Crafteado con un Bloque de Cobre en el centro.
  - Genera partículas naranja/moradas al bloquear.
  - Repele a los enemigos atacantes.

### 4. Botín del Mundo Alterado
- Todo el loot valioso en cofres de estructuras (Overworld, Nether, End) ha sido reemplazado por:
  - Lingotes de Cobre
  - Bloques de Cobre
  - **Manzanas de Cobre** (Custom)

## Comandos
- `/coppercurse help`: Muestra la ayuda del plugin.
- `/coppercurse reload`: Recarga la configuración (`config.yml`).
- `/coppercurse toggle`: Activa o desactiva las mecánicas del plugin en tiempo real.

## Requisitos
- **API**: Paper 1.21.x
- **Java**: 21 o superior
- **Resource Pack**: Se recomienda un resource pack que soporte los `CustomModelData` definidos en el plugin para visualizar las texturas custom.

## Instalación
1. Descarga el JAR desde la sección de lanzamientos o compílalo tú mismo.
2. Coloca el archivo en la carpeta `plugins/` de tu servidor Paper.
3. Reinicia el servidor.
4. Ajusta la configuración en `plugins/JDAR-CopperCurse/config.yml` si es necesario.

## Desarrollo
Este proyecto utiliza **Gradle (Kotlin DSL)** y sigue principios **SOLID**.
- Compilar: `./gradlew build`
- Tests: `./gradlew test`
