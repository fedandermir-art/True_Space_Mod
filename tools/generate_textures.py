#!/usr/bin/env python3
"""Deterministic pixel-art texture generator for True Space Mod.

Pure stdlib (no PIL required) so it runs anywhere Python 3 does.
Outputs are 16x16 RGBA PNGs — the standard Minecraft block texture size.

Run:
    python3 tools/generate_textures.py

Regenerate any time a texture needs tweaking; every texture below uses a
fixed seed so the output is reproducible.
"""
from __future__ import annotations

import random
import struct
import zlib
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
TEXTURE_DIR = ROOT / "src/main/resources/assets/truespace/textures/block"

SIZE = 16


def write_png(path: Path, pixels: list[list[tuple[int, int, int, int]]]) -> None:
    """Minimal PNG writer (8-bit RGBA, no interlace)."""
    w = h = len(pixels)

    def chunk(tag: bytes, data: bytes) -> bytes:
        out = struct.pack(">I", len(data)) + tag + data
        out += struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)
        return out

    raw = bytearray()
    for row in pixels:
        raw.append(0)  # filter: none
        for (r, g, b, a) in row:
            raw += struct.pack("BBBB", r, g, b, a)

    png = b"\x89PNG\r\n\x1a\n"
    png += chunk(b"IHDR", struct.pack(">IIBBBBB", w, h, 8, 6, 0, 0, 0))
    png += chunk(b"IDAT", zlib.compress(bytes(raw), 9))
    png += chunk(b"IEND", b"")
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_bytes(png)


def _clamp(v: int) -> int:
    return max(0, min(255, v))


def gen_lab_panel(seed: int = 42) -> list[list[tuple[int, int, int, int]]]:
    """Striped steel lab wall panel.

    Layout (16px): 4 vertical plates, 4px wide each:
      - 1px dark seam column (x % 4 == 0) — the panel joint groove
      - 3px plate (x % 4 == 1..3): left edge shaded, middle highlight line,
        right edge shadow into the seam — reads as a raised rib
    - alternating plate base brightness (even plates lighter) => the stripe look
    - vertical gradient: bright at top, dark at bottom (light from above)
    - 2x2 rivets at the top and bottom of each plate
    - per-pixel noise + a few brighter "brushed" scratches
    """
    rng = random.Random(seed)
    seam = (40, 43, 51)
    # cool (slightly blue) steel; strongly alternating plates = the stripe look
    plate_bases = [
        (170, 174, 184),
        (106, 111, 124),
        (162, 167, 178),
        (112, 117, 129),
    ]
    # rivet quad: TL brightest (light from above), BR darkest
    rivet = [(216, 220, 228), (184, 189, 200), (146, 151, 162), (88, 92, 103)]

    img: list[list[tuple[int, int, int, int]]] = []
    for y in range(SIZE):
        grad = 26 - 56 * (y / (SIZE - 1))  # +26 top  ->  -30 bottom
        row: list[tuple[int, int, int, int]] = []
        for x in range(SIZE):
            plate = x // 4
            sx = x % 4
            if sx == 0:
                c = seam
            else:
                base = plate_bases[plate]
                col_off = (-12, 14, -18)[sx - 1]
                c = tuple(int(b + grad + col_off) for b in base)
                # rivets occupy the two middle columns, rows 1..2 and 13..14
                if sx in (1, 2) and (y in (1, 2) or y in (13, 14)):
                    idx = (0 if y == 1 else 2) + (0 if sx == 1 else 1)
                    c = rivet[idx]
            j = rng.randint(-6, 6)
            row.append((_clamp(c[0] + j), _clamp(c[1] + j), _clamp(c[2] + j), 255))
        img.append(row)

    # a few brushed-metal scratches
    for _ in range(6):
        x = rng.randint(1, SIZE - 2)
        y = rng.randint(4, 12)
        r, g, b, a = img[y][x]
        img[y][x] = (_clamp(r + 20), _clamp(g + 20), _clamp(b + 20), a)
    return img


def main() -> None:
    write_png(TEXTURE_DIR / "lab_panel.png", gen_lab_panel())
    print(f"wrote {TEXTURE_DIR / 'lab_panel.png'}")


if __name__ == "__main__":
    main()
