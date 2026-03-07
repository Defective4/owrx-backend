# OWRX Backend

Backend reimplementation of OpenWebRX

## What is this project?

This project aims to implement most of [OpenWebRX+](https://github.com/luarvique/openwebrx)'s essential server-side functionality while keeping it compatible with owrx's frontend, custom clients, and online receiver catalogues.

> [!WARNING]
> 
> While this project aims to be compatible with OpenWebRX+, it's not meant as an alternative or replacement for it. I'm not planning to implement all of owrx's features, and even the ones that I will implement may not work as well as owrx's counterparts.
> 
> If you want to host your own online SDR, please take a look at [OpenWebRX+'s homepage](https://fms.komkon.org/OWRX/).
> 
> This project was made just for fun, it may be unstable or not usable at all in some cases, use at your own risk.

# Goals of OWRX Backend

| Icon | Meaning                                      |
| ---- | -------------------------------------------- |
| ✅    | Implemented                                  |
| ❓    | Might not be able to implement / help needed |
| ❌    | Won't implement                              |

The goals of this project are as follows:
- Implement the protocol used between OpenWebRX+'s frontend and backend.
- ✅ Seamlessly serve OpenWebRX+'s __unmodified__ frontend (cloned directly from [owrx's GitHub repository](https://github.com/luarvique/openwebrx), found in the [/htdocs](https://github.com/luarvique/openwebrx/tree/master/htdocs) directory)
- Implement basic analog modes using CSDR or GnuRadio (TBD). This includes:
  - Narrow- and Wideband FM
  - AM
  - SSB
  - CW
  - RAW
- Implement UI elements, such as:
  - Chat
  - Client count (and limit)
  - ❓ CPU usage and temperature
- Support adding bookmarks, dial frequencies, and bandplan
  - ❓ Possibly integrate with GQRX/SDR++ bandplan formats
- Generate, and serve FFT data
- Allow adjusting starting receiver parameters, including waterfall levels, theme, squelch level, tuning step.
- Allow configuring multiple SDR and SDR profiles (via config files)
- RDS, either with `gr-rds` (if I end up using GnuRadio), or [redsea](https://github.com/windytan/redsea) (what OpenWebRX+ uses currently)
- Integration with RTL-SDR, RTL-TCP, and file sources
- Free tuning (with and without a magic key)
- Integration with [receiverbook](https://www.receiverbook.de/) and other receiver listings

## What isn't planned
Things that may be added at some point, but generally are of low priority.
- Admin panel
- Files viewer
- Map view
- Digital modes
  - There will be an attempt to include modes supported by `multimon-ng`, `wsjtx`, + RTTY, but I might drop the idea
- Support for any SDR other than RTL-SDR
  - If I use GnuRadio, any SDR supported by `gr-osmosdr` *might* work, but because I don't have the hardware, I can't test anything other than rtl-sdr
- ADPCM compression
  - This one might actually get added soon after I get FFT and audio to work, but it's not guaranteed, because I don't have much knowledge about ADPCM encoding
- Automatic spotting and reporting
- Background decoding

# Contributing
See any issues? Go ahead, report them [here](https://github.com/Defective4/owrx-backend/issues)!  
Pull requests are also welcome.

# Support
If you want to support my work, consider buying me a coffee at [ko-fi.com!](https://ko-fi.com/defective4_)
