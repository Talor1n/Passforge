# Passforge

Passforge — a secure and lightweight password manager for Windows, designed to help you store, manage, and protect your credentials with ease. Built with robust Bouncy Castle encryption and a sleek FlatLaf interface, Passforge ensures your sensitive data stays safe while providing a seamless user experience. It comes bundled with JRE 21 (Azul Zulu) for hassle-free setup—no additional dependencies required.

**Note**: This is an alpha version of Passforge, developed as a college project. The project may continue to evolve with new features, improvements, and updates in the future.

## Key Features
- 🔒 **Military-Grade Encryption**: Secure your passwords and sensitive data using the Bouncy Castle cryptographic library.
- 🖥️ **Modern Interface**: Enjoy a clean and intuitive UI powered by FlatLaf, with support for light and dark themes.
- 📦 **Portable and Ready-to-Use**: Includes JRE 21, so you can run Passforge on any Windows system without installing Java.
- 🔄 **Easy Data Management**: Organize your credentials into categories, search, and export/import your data securely.

## Technologies
Passforge is built using the following technologies:
- **Java 21** (Azul Zulu JRE for runtime)
- **FlatLaf** (for a modern, cross-platform UI)
- **Bouncy Castle** (for encryption and security)
- **Gradle** (for build automation)
- **Launch4j** (for creating a native Windows executable)

## Installation
1. Download the latest release from the [Releases page](https://github.com/[your-username]/passforge/releases).
2. Extract the archive.
3. Run `Passforge.exe`—no additional setup required, as JRE 21 is included.

## Usage
1. Launch Passforge by double-clicking `Passforge.exe`.
2. Create a new vault or import an existing one.
3. Add your credentials, organize them into categories, and let Passforge encrypt and store them securely.
4. Use the search bar to quickly find passwords when needed.

## Building from Source
If you'd like to build Passforge from source, follow these steps:
1. Clone the repository:
   ```bash
   git clone https://github.com/Talor1n/Passforge
   cd passforge
   ```
2. Build the project using Gradle:
   ```bash
   ./gradlew buildWindows
   ```
3. **Add JRE 21 (Azul Zulu)**:
    - Download the JRE 21 for Windows (x64) from the [Azul Zulu website](https://www.azul.com/downloads/?package=jre).
    - Extract the downloaded ZIP archive.
    - Move the extracted JRE folder to `build/windows/`.
    - Rename the folder to `jre` (e.g., rename `zulu21.38.21-ca-jre21.0.5-win_x64` to `jre`).
4. After completing the steps above, your `build/windows/` directory should have the following structure:
   ```
   build/windows/
     Passforge.exe
     jre/
   ```

## License
Passforge is licensed under the [MIT License](LICENSE).  
Copyright © 2025 Talor1n.

## Support
If you encounter any issues or have suggestions, feel free to:
- Open an issue on the [Issues page](https://github.com/Talor1n/Passforge/issues).

## Acknowledgments
- Built with ❤️ by Talor1n.