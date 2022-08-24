package com.lukekaalim.mods.fresh_air;

public interface CameraOrbiter {
  void pushCameraDistance(float distance);

  void rotateCamera(float yaw, float pitch);

  void setCameraYaw(float yaw);
  void setCameraPitch(float pitch);

  float getCameraYaw();
}