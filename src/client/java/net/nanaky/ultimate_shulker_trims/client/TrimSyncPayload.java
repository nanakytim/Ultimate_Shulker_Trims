package net.nanaky.ultimate_shulker_trims.client;

import net.nanaky.ultimate_shulker_trims.ShulkerTrim;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public record TrimSyncPayload(int x, int y, int z, @Nullable ShulkerTrim trim)
    implements CustomPacketPayload {

  public static final Identifier CHANNEL_ID =
      Identifier.fromNamespaceAndPath("ultimate_shulker_trims", "sync");
  public static final CustomPacketPayload.Type<TrimSyncPayload> TYPE =
      new CustomPacketPayload.Type<>(CHANNEL_ID);
  public static final StreamCodec<FriendlyByteBuf, TrimSyncPayload> STREAM_CODEC =
      StreamCodec.of(
          (buf, payload) -> {
            buf.writeInt(payload.x());
            buf.writeInt(payload.y());
            buf.writeInt(payload.z());
            buf.writeBoolean(payload.trim() != null);
            if (payload.trim() != null) {
              buf.writeUtf(payload.trim().pattern());
              buf.writeUtf(payload.trim().material());
            }
          },
          buf -> {
            int x = buf.readInt();
            int y = buf.readInt();
            int z = buf.readInt();
            boolean hasTrim = buf.readBoolean();
            ShulkerTrim trim = null;
            if (hasTrim) {
              trim = new ShulkerTrim(buf.readUtf(), buf.readUtf());
            }
            return new TrimSyncPayload(x, y, z, trim);
          });

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}