# Birthday Calendar Converter

A Kotlin Multiplatform (KMP) WASM application that converts VCF (vCard) files to ICS (iCalendar) format, specifically extracting birthdays and creating calendar events.

## Features

✨ **VCF to ICS Conversion** - Upload a VCF file and convert birthdays to calendar format
🎂 **Smart Birthday Detection** - Automatically extracts names and birthdays from vCard format
📅 **Flexible Event Options**:
  - **With Age**: Generate events for a specific year with age (e.g., "Aerith Gainsborough's 30th Birthday")
  - **Recurring**: Create yearly recurring events without age (e.g., "Aerith Gainsborough's Birthday")
⬇️ **Easy Download** - Download the generated ICS file to import into your calendar app

## Architecture

Built using **MVI (Model-View-Intent)** and **Clean Architecture** patterns:

### Domain Layer
- **Models**: `Birthday`, `EventConfig`, `BirthdayEvent`
- **Use Cases**: 
  - `ParseVcfUseCase` - Parses VCF content and extracts birthdays
  - `GenerateIcsUseCase` - Generates ICS calendar file

### Presentation Layer
- **State**: `BirthdaysState` (sealed class for Upload, Overview, Done, Error states)
- **Intent**: `BirthdaysIntent` (user actions)
- **ViewModel**: `BirthdaysViewModel` (manages state and processes intents)

### Platform Layer
- **FileHandler**: Platform-specific file operations (expect/actual pattern)
- **WASM Implementation**: Browser-based file picker and download

## User Flow

### Step 1: Upload
- Upload or select a .vcf (vCard) file
- Supports drag & drop and file picker
- Displays instructions and examples

### Step 2: Overview
- Shows number of birthdays detected
- Choose event format:
  - **Option 1**: Include age with year selection dropdown (2026-2036)
  - **Option 2**: Recurring yearly events
- Preview shows how events will be named
- Generate calendar button to proceed

### Step 3: Done
- Success confirmation
- Download button to save the ICS file
- "Convert Another File" button to start over

## Supported VCF Formats

The parser supports multiple birthday date formats:
- `YYYY-MM-DD` (with year)
- `YYYYMMDD` (with year)
- `--MM-DD` (without year)
- `--MMDD` (without year)

## Testing

A sample VCF file is provided at `test-birthdays.vcf` with 4 test contacts:
1. John Smith - Birthday 08-10 (without year)
2. Jane Doe - Born 1990-12-25 (with year)

## Building

```bash
./gradlew :birthdays:build
```

## Running

The birthdays module can be integrated into the main WASM application. Navigate to the birthdays screen to use the converter.

## Generated ICS Format

The generated ICS file follows RFC 5545 standards:
- VCALENDAR with VERSION 2.0
- VEVENT blocks for each birthday
- RRULE for recurring events
- Proper date formatting (YYYYMMDD)
- Categories set to "Birthday"

## Tech Stack

- **Kotlin Multiplatform** - Cross-platform development
- **Compose Multiplatform** - UI framework
- **WASM** - Web deployment
- **Material 3** - Modern UI components
- **Lifecycle ViewModel** - State management
- **Kotlin Coroutines** - Async operations

## Project Structure

```
birthdays/
├── src/
│   ├── commonMain/kotlin/io/dontsayboj/birthdays/
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   ├── Birthday.kt
│   │   │   │   ├── EventConfig.kt
│   │   │   │   └── BirthdayEvent.kt
│   │   │   └── usecase/
│   │   │       ├── ParseVcfUseCase.kt
│   │   │       └── GenerateIcsUseCase.kt
│   │   ├── presentation/
│   │   │   ├── BirthdaysState.kt
│   │   │   ├── BirthdaysIntent.kt
│   │   │   └── BirthdaysViewModel.kt
│   │   ├── platform/
│   │   │   └── FileHandler.kt (expect)
│   │   ├── ui/
│   │   │   ├── upload/UploadScreen.kt
│   │   │   ├── overview/OverviewScreen.kt
│   │   │   └── done/DoneScreen.kt
│   │   └── BirthdaysScreen.kt
│   └── wasmJsMain/kotlin/io/dontsayboj/birthdays/
│       └── platform/
│           └── FileHandler.wasmJs.kt (actual)
├── test-birthdays.vcf (sample file)
└── README.md
```

## License

Part of the DontSayBojio KMP project.
